# MVC 프레임워크 만들기

## Front-Controller Pattern ?

프론트 컨트롤러를 도입하기 전의 상황
![before-front-controller.png](imgs/before-front-controller.png)

- 입구가 없는 것임.
- 그래서 공통 로직을 요청마다 다 만들어야한다는 것임
- View로 이동하는 공통 로직도 다 넣어야 한다는 것


- 프론트 컨트롤러를 등장 두둥
  ![front-controller.png](imgs/front-controller.png)

#### 프론트 컨트롤러 패턴의 특징

- 프론트 컨트롤러 서블릿 하나로 클라이언트의 요청을 받음
- 프론트 컨트롤러가 요청에 맞는 컨트롤러를 찾아서 호출
- 입구를 하나로!(수.문.장)
- 공통 처리 가능
- 프론트 컨트롤러를 제외한 나머지 컨트롤러는 서블릿을 사용하지 않아도 됨!!
    - 요청 매핑을 했을 때 서블릿을 사용했지.
    - 클라이언트에서 요청이 오면 WAS 서버에서 가장 먼저 요청을 받는 것이 FrontController
    - 그래서 나머지 컨트롤러는 이제 서블릿을 사용하지 않아도 됩니다.

> 스프링 웹 MVC와 프론트 컨트롤러

- 스프링 웹 MVC의 핵심도 바로 FrontController
- 스프링 웹 MVC의 DispatcherServlet이 FrontController 패턴으로 구현되어 있음!!

---

자, 우선 기존의 구조를 완전히 똑같이 가져가면서 프론트 컨트롤러를 도입해보자.

Controller Interface

````java
public interface ControllerV1 {

    void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
}
````

이 인터페이스를 상속받은 각 컨트롤러들

- Form

````java
public class MemberFormControllerV1 implements ControllerV1 {

    @Override
    public void process(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        String viewPath = "/WEB-INF/views/new-form.jsp";
        final RequestDispatcher dispatcher = request.getRequestDispatcher(viewPath);// 컨트롤러에서 뷰로 이동할 때 씀
        dispatcher.forward(request, response); // 이걸 호출하면, 실제로 Controller -> view로 이동하게 됨 (서버 내부에서 서버끼리 호출하는 형태임)
    }
}
````

- Save

```java
public class MemberSaveControllerV1 implements ControllerV1 {
    private MemberRepository memberRepository = MemberRepository.getInstance();

    @Override
    public void process(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        final String username = request.getParameter("username");
        final int age = Integer.parseInt(request.getParameter("age"));

        final Member member = new Member(username, age);
        memberRepository.save(member);
        request.setAttribute("member", member);

        final String viewPath = "/WEB-INF/views/save-result.jsp";
        final RequestDispatcher dispatcher = request.getRequestDispatcher(viewPath);
        dispatcher.forward(request, response);
    }
}
```

- List

```java
public class MemberListControllerV1 implements ControllerV1 {

    private MemberRepository memberRepository = MemberRepository.getInstance();

    @Override
    public void process(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        final List<Member> members = memberRepository.findAll();

        request.setAttribute("members", members);

        final String viewPath = "/WEB-INF/views/members.jsp";
        final RequestDispatcher dispatcher = request.getRequestDispatcher(viewPath);
        dispatcher.forward(request, response);
    }
}
```

- 프론트 컨트롤러

````java

@WebServlet(name = "frontControllerServletV1", urlPatterns = "/front-controller/v1/*") // /front-controller/v1 하위의 어떠한 url이든 다 이 서블릿이 받도록 하겠다.
public class FrontControllerServletV1 extends HttpServlet {

    private Map<String, ControllerV1> controllerMap = new HashMap<>();

    public FrontControllerServletV1() {
        controllerMap.put("/front-controller/v1/members/new-form", new MemberFormControllerV1());
        controllerMap.put("/front-controller/v1/members/save", new MemberSaveControllerV1());
        controllerMap.put("/front-controller/v1/members", new MemberListControllerV1());
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {

        final String requestURI = request.getRequestURI();

        final ControllerV1 controller = controllerMap.get(requestURI);
        if (controller == null) {
            response.setStatus(SC_NOT_FOUND);
            return;
        }

        controller.process(request, response);
    }
}
````

#### 프론트 컨트롤러 분석

> urlPatterns

- urlPatterns = "/front-controller/v1/*" : /front-controller/v1 를 포함한 하위 모든 요청은 이 서블릿에서 받아들인다.
- 예) /front-controller/v1 , /front-controller/v1/a , /front-controller/v1/a/b

> controllerMap

- key: 매핑 URL
- value: 호출될 컨트롤러

> service()

- 먼저 requestURI 를 조회해서 실제 호출할 컨트롤러를 controllerMap 에서 찾는다. 만약 없다면 404(SC_NOT_FOUND) 상태 코드를 반환한다.
- 컨트롤러를 찾고 controller.process(request, response); 을 호출해서 해당 컨트롤러를 실행한다.

> JSP

- JSP는 이전 MVC에서 사용했던 것을 그대로 사용한다.

> 개발 팁

- 어떤 구조 개선을 하게 되는 프로젝트들이 많을 것이다.
- 구조 개선을 하다보면, `아 이것도 이렇게 바꿀 수 있을 것 같은데?`와 같은 생각이 들기 마련이다.(내가 요새 하고 있는 것이라 너무 공감 중)
- **한 번 참아야 한다!!!!!!!!!!!!!**
- 우선 같은 레벨(구조 개선)에 집중하여 구조를 먼저 개선한 뒤, 바뀐 구조에 대해서 동작에 이상이 없으면 그 이후에 리팩토링 및 개선을 하는 것이 옳다.

--- 

## V2 - 이번에는 뷰를 분리해보자

모든 컨트롤러에서 뷰로 이동하는 부분에 중복이 있고, 깔끔하지 않다.

```java
String viewPath="/WEB-INF/views/new-form.jsp";
        RequestDispatcher dispatcher=request.getRequestDispatcher(viewPath);
        dispatcher.forward(request,response);
```

이 부분을 깔끔하게 분리하기 위해 별도로 뷰를 처리하는 객체를 만들자.
![frontcontrollerV2.png](imgs/frontcontrollerV2.png)

```java
public interface ControllerV2 {
    MyView process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
}
```

```java
public class MemberFormControllerV2 implements ControllerV2 {

    @Override
    public MyView process(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        return new MyView("/WEB-INF/views/new-form.jsp");
    }
}
```

```java
public class MemberListControllerV2 implements ControllerV2 {
    private MemberRepository memberRepository = MemberRepository.getInstance();

    @Override
    public MyView process(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        final List<Member> members = memberRepository.findAll();
        request.setAttribute("members", members);
        return new MyView("/WEB-INF/views/members.jsp");
    }
}
```

```java
public class MemberSaveControllerV2 implements ControllerV2 {
    private MemberRepository memberRepository = MemberRepository.getInstance();

    @Override
    public MyView process(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        final String username = request.getParameter("username");
        final int age = Integer.parseInt(request.getParameter("age"));

        final Member member = new Member(username, age);
        memberRepository.save(member);
        request.setAttribute("member", member);

        return new MyView("/WEB-INF/views/save-result.jsp");
    }
}
```

V2 FrontController

```java@WebServlet(name = "frontControllerServletV2", urlPatterns = "/front-controller/v2/*") // /front-controller/v2 하위의 어떠한 url이든 다 이 서블릿이 받도록 하겠다.
public class FrontControllerServletV2 extends HttpServlet {

    private Map<String, ControllerV2> controllerMap = new HashMap<>();

    public FrontControllerServletV2() {
        controllerMap.put("/front-controller/v2/members/new-form", new MemberFormControllerV2());
        controllerMap.put("/front-controller/v2/members/save", new MemberSaveControllerV2());
        controllerMap.put("/front-controller/v2/members", new MemberListControllerV2());
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {

        final String requestURI = request.getRequestURI();

        final ControllerV2 controller = controllerMap.get(requestURI);
        if (controller == null) {
            response.setStatus(SC_NOT_FOUND);
            return;
        }

        controller.process(request, response).render(request, response);
    }
}
```

---

## Model 추가 - v3 : 이번에는 서블릿 종속성을 제거해보자

- 컨트롤러 입장에서 HttpServletRequest, HttpServletResponse이 꼭 필요할까? 안 쓰는 곳도 있는 걸!?
- 요청 파라미터 정보는 자바의 Map으로 대신 넘기도록 하면 지금 구조에서는 컨트롤러가 서블릿 기술을 몰라도 동작할 수 있다.
- 그리고 request 객체를 Model로 사용하는 대신에 별도의 Model 객체를 만들어서 반환하면 된다.
- 우리가 구현하는 컨트롤러가 서블릿 기술을 전혀 사용하지 않도록 변경해보자!!
- 이렇게 하면 구현 코드도 매우 단순해지고, 테스트 코드 작성이 쉽다.

> 뷰 이름 중복 제거도 해보자

- 컨트롤러에서 지정하는 뷰 이름에 중복이 있는 것을 확인할 수 있다.
- 컨트롤러는 뷰의 논리 이름을 반환하고, 실제 물리 위치의 이름은 프론트 컨트롤러에서 처리하도록 단순화 하자.
- 이렇게 해두면 향후 뷰의 폴더 위치가 함께 이동해도 프론트 컨트롤러만 고치면 된다.
- `/WEB-INF/views/new-form.jsp` -> `new-form`
- `/WEB-INF/views/save-result.jsp` -> `save-result`
- `/WEB-INF/views/members.jsp` -> `members`
  ![frontcontrollerV3.png](imgs/frontcontrollerV3.png)

### ModelView

- 지금까지 컨트롤러에서 서블릿에 종속적인 `HttpServletRequest`를 사용했다.
- 그리고 Model도 `request.setAttribute()`를 통해 데이터를 저장하고 뷰에 전달했다.
- 서블릿의 종속성을 제거하기 위해 Model을 직접 만들고, 추가로 View 이름까지 전달하는 객체를 만들어보자.
  (이번 버전에서는 컨트롤러에서 HttpServletRequest를 사용할 수 없다. 따라서 직접 `request.setAttribute()`를 호출할 수 도 없다. 따라서 Model이 별도로 필요하다.)

코드를 한 번 살펴보자

```java
public interface ControllerV3 {
    ModelView process(Map<String, String> paramMap); // 서블릿 기술이 하나도 없다!(서블릿에 종속적이지 않다!!)
}
```

요 ModelView가 무엇인고?

```java
public class ModelView { // SpringMVC에는 비슷한 기능의 ModelAndView 라는 것이 있다.
    private String viewName;
    private Map<String, Object> model = new HashMap<>();

    public ModelView(final String viewName) {
        this.viewName = viewName;
    }

    public String getViewName() {
        return viewName;
    }

    public Map<String, Object> getModel() {
        return model;
    }

    public void setViewName(final String viewName) {
        this.viewName = viewName;
    }

    public void setModel(final Map<String, Object> model) {
        this.model = model;
    }
}
```

- 말 그대로, 모델과 뷰를 동시에 저장하는 객체다.

- MemberFormController

````java
public class MemberFormControllerV3 implements ControllerV3 {
    @Override
    public ModelView process(final Map<String, String> paramMap) {
        return new ModelView("new-form"); // WEB-INF/views 와 .jsp를 뺀 논리명만
    }
}
````

- MemberSaveController

```java
public class MemberSaveControllerV3 implements ControllerV3 {

    private MemberRepository memberRepository = MemberRepository.getInstance();

    @Override
    public ModelView process(final Map<String, String> paramMap) {
        final String username = paramMap.get("username");
        final int age = Integer.parseInt(paramMap.get("age"));

        final Member member = new Member(username, age);
        memberRepository.save(member);

        final ModelView mv = new ModelView("save-result");
        mv.getModel().put("member", member);
        return mv;
    }
}
```

- MemberListController

```java
public class MemberListControllerV3 implements ControllerV3 {

    private MemberRepository memberRepository = MemberRepository.getInstance();

    @Override
    public ModelView process(final Map<String, String> paramMap) {
        final List<Member> members = memberRepository.findAll();
        final ModelView mv = new ModelView("members");
        mv.getModel().put("members", members);
        return mv;
    }
}
```

이제 대망의 FrontController는 다음과 같다.

```java

@WebServlet(name = "frontControllerServletV3", urlPatterns = "/front-controller/v3/*") // /front-controller/v3 하위의 어떠한 url이든 다 이 서블릿이 받도록 하겠다.
public class FrontControllerServletV3 extends HttpServlet {

    private Map<String, ControllerV3> controllerMap = new HashMap<>();

    public FrontControllerServletV3() {
        controllerMap.put("/front-controller/v3/members/new-form", new MemberFormControllerV3());
        controllerMap.put("/front-controller/v3/members/save", new MemberSaveControllerV3());
        controllerMap.put("/front-controller/v3/members", new MemberListControllerV3());
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {

        final String requestURI = request.getRequestURI();
        final ControllerV3 controller = controllerMap.get(requestURI);
        if (controller == null) {
            response.setStatus(SC_NOT_FOUND);
            return;
        }

        Map<String, String> paramMap = createParamMap(request); // Request에 있는 파라미터를 다 뽑아오자
        final ModelView mv = controller.process(paramMap); // 논리 이름을 물리 이름으로 바꿔주는 viewResolver가 필요하다

        // mv.getViewName(); // 논리 이름 ex) new-form
        final String viewName = mv.getViewName();
        final MyView view = viewResolver(viewName); // 완성된 실제 물리 View 이름!

        view.render(mv.getModel(), request, response);
    }

    private MyView viewResolver(final String viewName) {
        return new MyView("/WEB-INF/views/" + viewName + ".jsp");
    }

    private Map<String, String> createParamMap(final HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<>();
        request.getParameterNames().asIterator()
               .forEachRemaining(paramName -> paramMap.put(paramName, request.getParameter(paramName)));
        return paramMap;
    }
}
```

- view에서 이제 render를 할 때, request에 model 데이터를 모두 담아서 보내야할 것이다.
- 그래야 jsp에서 ${ }로 값을 받아서 쓸 테니.

- createParamMap()
    - HttpServletRequest에서 파라미터 정보를 꺼내서 Map으로 변환한다. 그리고 해당 Map( paramMap )을 컨트롤러에 전달하면서 호출한다.

- 뷰 리졸버
    - MyView view = viewResolver(viewName)
    - 컨트롤러가 반환한 논리 뷰 이름을 실제 물리 뷰 경로로 변경한다.
    - 그리고 실제 물리 경로가 있는 MyView 객체를 반환한다.
        - 논리 뷰 이름: members
        - 물리 뷰 경로: /WEB-INF/views/members.jsp

- view.render(mv.getModel(), request, response)
    - 뷰 객체를 통해서 HTML 화면을 렌더링 한다.
    - 뷰 객체의 render() 는 모델 정보도 함께 받는다.
    - JSP는 request.getAttribute() 로 데이터를 조회하기 때문에, 모델의 데이터를 꺼내서 request.setAttribute() 로 담아둔다.
    - JSP로 포워드 해서 JSP를 렌더링 한다.

그래서 View는 다음과 같이 구현한다.

```java
public class MyView {
    private String viewPath;

    public MyView(final String viewPath) {
        this.viewPath = viewPath;
    }

    public void render(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final RequestDispatcher dispatcher = request.getRequestDispatcher(viewPath);
        dispatcher.forward(request, response);
    }

    public void render(final Map<String, Object> model, final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        modelToRequestAttribute(model, request); // 모델에 담은 값을 Request에 담아주고
        render(request, response); // 기존에 하던 렌더 방식을 취한다.
    }

    private void modelToRequestAttribute(final Map<String, Object> model, final HttpServletRequest request) {
        model.forEach((key, value) -> request.setAttribute(key, value));
    }
}
```

- 결과적으로 프론트 컨트롤러가 할 일이 많아졌다.
- 그런데 장점은 뭐냐? 실제 호출하는 컨트롤러가 굉장히 심플하다!
- 자자, 다시 한번 구조를 상기하자.
  ![frontcontrollerV3.png](imgs/frontcontrollerV3.png)

## 단순하고 실용적인 컨트롤러 - v4

- 앞서 만든 v3 컨트롤러는 서블릿 종속성을 제거하고 뷰 경로의 중복을 제거하는 등, 잘 설계된 컨트롤러이다.
- 그런데 실제 컨트톨러 인터페이스를 구현하는 개발자 입장에서 보면, 항상 ModelView 객체를 생성하고 반환해야 하는 부분이 조금은 번거롭다.
- 좋은 프레임워크는 아키텍처도 중요하지만, 그와 더불어 실제 개발하는 개발자가 단순하고 편리하게 사용할 수 있어야 한다. 소위 실용성이 있어야 한다.
- 이번에는 v3를 조금 변경해서 실제 구현하는 개발자들이 매우 편리하게 개발할 수 있는 v4 버전을 개발해보자.

![v4.png](imgs/v4.png)

- ModelView를 반환하지 않고, ViewName만을 반환해보자!!

- Controller Interface

```java
public interface ControllerV4 {
    String process(Map<String, String> paramMap, Map<String, Object> model);
}
```

- MemberForm

````java
public class MemberFormControllerV4 implements ControllerV4 {
    @Override
    public String process(final Map<String, String> paramMap, final Map<String, Object> model) {
        return "new-form";
    }
}
````

- MemberSave

````java
public class MemberSaveControllerV4 implements ControllerV4 {

    private MemberRepository memberRepository = MemberRepository.getInstance();

    @Override
    public String process(final Map<String, String> paramMap, final Map<String, Object> model) {
        final String username = paramMap.get("username");
        final int age = Integer.parseInt(paramMap.get("age"));

        final Member member = new Member(username, age);
        memberRepository.save(member);

        model.put("member", member);
        return "save-result";
    }
}
````

- MemberList

````java
public class MemberListControllerV4 implements ControllerV4 {

    private MemberRepository memberRepository = MemberRepository.getInstance();

    @Override
    public String process(final Map<String, String> paramMap, final Map<String, Object> model) {
        final List<Member> members = memberRepository.findAll();
        model.put("members", members);
        return "members";
    }
}
````

- FrontController

```java

@WebServlet(name = "frontControllerV4", urlPatterns = "/front-controller/v4/*")
public class FrontControllerServletV4 extends HttpServlet {

    private Map<String, ControllerV4> controllerMap = new HashMap<>();

    public FrontControllerServletV4() {
        controllerMap.put("/front-controller/v4/members/new-form", new MemberFormControllerV4());
        controllerMap.put("/front-controller/v4/members/save", new MemberSaveControllerV4());
        controllerMap.put("/front-controller/v4/members", new MemberListControllerV4());
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        final String requestURI = request.getRequestURI();

        final ControllerV4 controller = controllerMap.get(requestURI);
        if (controller == null) {
            response.setStatus(SC_NOT_FOUND);
            return;
        }

        final Map<String, String> paramMap = createParamMap(request);
        final Map<String, Object> model = new HashMap<>(); // 기존에는 ModelAndView에서 모델을 가져왔지만, 이번엔 프론트 컨트롤러에서 모델을 제공하는 것의 차이(V3과의 차이)

        final String viewName = controller.process(paramMap, model);

        MyView view = viewResolver(viewName);

        view.render(model, request, response);
    }

    private MyView viewResolver(final String viewName) {
        return new MyView("/WEB-INF/views/" + viewName + ".jsp");
    }

    private Map<String, String> createParamMap(final HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<>();
        request.getParameterNames().asIterator()
               .forEachRemaining(paramName -> paramMap.put(paramName, request.getParameter(paramName)));
        return paramMap;
    }
}
```

---

### v5 - 유연한 컨트롤러를 만들어보자

- 어댑터를 통해서 컨트롤러를 호출 한다는 개념을 적용해보자
- 핸들러를 처리할 수 있는 핸들러 어댑터를 조회한 다음, 핸들러 어댑터를 통해 컨트롤러를 호출!!
  ![v5.png](imgs/v5.png)

- 핸들러 어댑터: 중간에 어댑터 역할을 하는 어댑터가 추가되었는데 이름이 핸들러 어댑터이다. 여기서 어댑터 역할을 해주는 덕분에 다양한 종류의 컨트롤러를 호출할 수 있다.
- 핸들러: 컨트롤러의 이름을 더 넓은 범위인 핸들러로 변경했다. 그 이유는 이제 어댑터가 있기 때문에 꼭 컨트롤러의 개념 뿐만 아니라 어떠한 것이든 해당하는 종류의 어댑터만 있으면 다 처리할 수 있기 때문이다.

`MyHandlerAdapter`

````java
package hello.servlet.web.frontcontroller.v5;

import hello.servlet.web.frontcontroller.ModelView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface MyHandlerAdapter {
    boolean supports(Object handler);

    ModelView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws ServletException, IOException;
}
````

`boolean supports(Object handler)`

- handler는 컨트롤러를 말한다.
- 어댑터가 해당 컨트롤러를 처리할 수 있는지 판단하는 메서드다.

`ModelView handle(HttpServletRequest request, HttpServletResponse response, Object handler)`

- 어댑터는 실제 컨트롤러를 호출하고, 그 결과로 ModelView를 반환해야 한다.
- 실제 컨트롤러가 ModelView를 반환하지 못하면, 어댑터가 ModelView를 직접 생성해서라도 반환해야 한다.
- 이전에는 프론트 컨트롤러가 실제 컨트롤러를 호출했지만 이제는 이 어댑터를 통해서 실제 컨트롤러가 호출된다.

> 우선, `front-controller/v5/*` 로 들어오는 요청을 한 번 어댑터로 처리해보자.

이번 예제에서는 V3과 V4를 어댑터로 받아와서 처리할 것이다.

어댑터의 규격이 있어야 할 것이다.

```java
public interface MyHandlerAdapter {

    boolean supports(Object handler);

    ModelView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws ServletException, IOException;
}
```

우선 지원하는 규격인지 판단하는 supports와, 실제 렌더링을 수행하는 handle을 정의했다.

이제 어댑터를 구현해보자.

V3 어댑터

```java
public class ControllerV3HandlerAdapter implements MyHandlerAdapter {
    @Override
    public boolean supports(final Object handler) {
        return (handler instanceof ControllerV3);
    }

    @Override
    public ModelView handle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) throws ServletException, IOException {
        final ControllerV3 controller = (ControllerV3) handler;

        final Map<String, String> paramMap = createParamMap(request);

        return controller.process(paramMap);
    }

    private Map<String, String> createParamMap(final HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<>();
        request.getParameterNames().asIterator()
               .forEachRemaining(paramName -> paramMap.put(paramName, request.getParameter(paramName)));
        return paramMap;
    }
}
```

- supports
    - 일단 지원하기 위해서는 `ControllerV3` 인터페이스 타입인지를 확인해야한다.

- handle
    - supports에 통과된 핸들러는 ControllerV3 타입으로 캐스팅을 한다.
    - 기존 V3는 request에 딸려있는 파라미터를 다 받아온 paramMap을 넘겨서, ModelView 객체를 만들어서, ModelView 내의 model에 데이터를 세팅하고, ModelView를 반환하면 이를 받아서 렌더하는 방식이였다.

```java
public class MemberListControllerV3 implements ControllerV3 {
    private MemberRepository memberRepository = MemberRepository.getInstance();
    
    @Override
    public ModelView process(final Map<String, String> paramMap) {
        final List<Member> members = memberRepository.findAll();
        final ModelView mv = new ModelView("members");
        mv.getModel().put("members", members);
        return mv;
    }
}
```

- 현재 어댑터의 리턴을 ModelView로 했으므로, 굉장히 간단하게 모델에 데이터를 담는 방식이다.


- V5 컨트롤러는 어떻게 생겨먹으면 될까

```java
@WebServlet(name = "frontControllerServletV5", urlPatterns = "/front-controller/v5/*")
public class FrontControllerServletV5 extends HttpServlet {

    private final Map<String, Object> handlerMappingMap = new HashMap<>();
    private final List<MyHandlerAdapter> handlerAdapters = new ArrayList<>();

    public FrontControllerServletV5() {
        initHandlerMappingMap(); // 어떤 요청들을 받을 지 세팅
        initHandlerAdapters(); // 요청 받은 버전에 대한 어댑터 세팅
    }

    private void initHandlerMappingMap() {
        handlerMappingMap.put("/front-controller/v5/v3/members/new-form", new MemberFormControllerV3());
        handlerMappingMap.put("/front-controller/v5/v3/members/save", new MemberSaveControllerV3());
        handlerMappingMap.put("/front-controller/v5/v3/members", new MemberListControllerV3());

        // v4 추가
        handlerMappingMap.put("/front-controller/v5/v4/members/new-form", new MemberFormControllerV4());
        handlerMappingMap.put("/front-controller/v5/v4/members/save", new MemberSaveControllerV4());
        handlerMappingMap.put("/front-controller/v5/v4/members", new MemberListControllerV4());
    }

    private void initHandlerAdapters() {
        handlerAdapters.add(new ControllerV3HandlerAdapter());
        handlerAdapters.add(new ControllerV4HandlerAdapter());
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {

        final Object handler = getHandler(request);

        if (handler == null) {
            response.setStatus(SC_NOT_FOUND);
            return;
        }

        final MyHandlerAdapter adapter = handlerAdapters.stream()
                                                        .filter(a -> a.supports(handler))
                                                        .findFirst()
                                                        .orElseThrow(() -> new IllegalArgumentException("handler Adapter를 찾을 수 없습니다. handler = " + handler));

        final ModelView mv = adapter.handle(request, response, handler);

        final String viewName = mv.getViewName();
        final MyView view = viewResolver(viewName);

        view.render(mv.getModel(), request, response);
    }

    private MyView viewResolver(final String viewName) {
        return new MyView("/WEB-INF/views/" + viewName + ".jsp"); // 논리 이름 -> 실제 물리 이름
    }

    private Object getHandler(final HttpServletRequest request) {
        final String requestURI = request.getRequestURI();
        return handlerMappingMap.get(requestURI);
    }
}
```

- 컨트롤러(Controller) -> 핸들러(Handler)
    - 이전에는 컨트롤러를 직접 매핑해서 사용했다.
    - 그런데 이제는 어댑터를 사용하기 때문에, 컨트롤러 뿐만 아니라 어댑터가 지원하기만 하면, 어떤 것이라도 URL에 매핑해서 사용할 수 있다.
    - 그래서 이름을 컨트롤러에서 더 넒은 범위의 핸들러로 변경했다.
- 매핑 정보의 값이 ControllerV3 , ControllerV4 같은 인터페이스에서 아무 값이나 받을 수 있는 Object 로 변경되었다.

- 내가 `/front-controller/v5/v3/members` 이와 같은 방식으로 요청을 했을 때
- 우선 핸들러 매핑에 있는지 확인하고, 해당 핸들러를 가져온다 . `MemberListControllerV3()`
- 그리고 이 핸들러를 처리할 수 있는 핸들러 어댑터를 조회한다.
- 핸들러 어댑터를 통해 실제 컨트롤러를 호출하는 `handle` 작업이 이뤄진다.
- 핸들러 어댑터는 ModelView를 반환한다.
- 여기서 viewName을 가져와서, viewResolver를 호출하여, 뷰를 가져온다.
- view에 model과 request, response를 담아서 렌더링한다.


