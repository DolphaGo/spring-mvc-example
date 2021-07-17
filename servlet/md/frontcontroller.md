#  MVC 프레임워크 만들기

## Front-Controller Pattern ?

프론트 컨트롤러를 도입하기 전의 상황
![before-front-controller.png](imgs/before-front-controller.png)

- 입구가 없는 것임.
- 그래서 공통 로직을 요청마다 다 만들어야한다는 것임
- View로 이동하는 공통 로직도 다 넣어야 한다는 것


프론트 컨트롤러를 등장 두둥
![front-controller.png](imgs/front-controller.p

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
