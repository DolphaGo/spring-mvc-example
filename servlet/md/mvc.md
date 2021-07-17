## MVC 패턴 - 개요

### 너무 많은 역할
- 하나의 서블릿이나 JSP만으로 비즈니스 로직과 뷰 렌더링까지 모두 처리하게 되면, 너무 많은 역할을 하게되고, 결과적으로 유지보수가 어려워진다. 
- 비즈니스 로직을 호출하는 부분에 변경이 발생해도 해당 코드를 손대야 하고, UI를 변경할 일이 있어도 비즈니스 로직이 함께 있는 해당 파일을 수정해야 한다. 
- HTML 코드 하나 수정해야 하는데, 수백줄의 자바 코드가 함께 있다고 상상해보라! 또는 비즈니스 로직을 하나 수정해야 하는데 수백 수천줄의 HTML 코드가 함께 있다고 상상해보라.


### 변경의 라이프 사이클
- 사실 이게 정말 중요한데, 진짜 문제는 둘 사이에 변경의 라이프 사이클이 다르다는 점이다. 
- 예를 들어서 UI 를 일부 수정하는 일과 비즈니스 로직을 수정하는 일은 각각 다르게 발생할 가능성이 매우 높고 대부분 서로에게 영향을 주지 않는다.
- 이렇게 변경의 라이프 사이클이 다른 부분을 하나의 코드로 관리하는 것은 유지보수하기 좋지 않다. (물론 UI가 많이 변하면 함께 변경될 가능성도 있다.)

### 기능 특화
- 특히 JSP 같은 뷰 템플릿은 화면을 렌더링 하는데 최적화 되어 있기 때문에 이 부분의 업무만 담당하는 것이 가장 효과적이다.

### Model View Controller
- MVC 패턴은 지금까지 학습한 것 처럼 하나의 서블릿이나, JSP로 처리하던 것을 컨트롤러(Controller)와 뷰(View)라는 영역으로 서로 역할을 나눈 것을 말한다. 
- 웹 애플리케이션은 보통 이 MVC 패턴을 사용한다.

- `컨트롤러`: HTTP 요청을 받아서 파라미터를 검증하고, 비즈니스 로직을 실행한다. 그리고 뷰에 전달할 결과 데이터를 조회해서 모델에 담는다.
- `모델`: 뷰에 출력할 데이터를 담아둔다. 뷰가 필요한 데이터를 모두 모델에 담아서 전달해주는 덕분에 뷰는 비즈니스 로직이나 데이터 접근을 몰라도 되고, 화면을 렌더링 하는 일에 집중할 수 있다.
- `뷰`: 모델에 담겨있는 데이터를 사용해서 화면을 그리는 일에 집중한다. 여기서는 HTML을 생성하는 부분을 말한다.

> 참고
- 컨트롤러에 비즈니스 로직을 둘 수도 있지만, 이렇게 되면 컨트롤러가 너무 많은 역할을 담당한다. 
- 그래서 일반적으로 비즈니스 로직은 서비스(Service)라는 계층을 별도로 만들어서 처리한다. 
- 그리고 컨트롤러는 비즈니스 로직이 있는 서비스를 호출하는 담당한다. 
- 참고로 비즈니스 로직을 변경하면 비즈니스 로직을 호출하는 컨트롤러의 코드도 변경될 수 있다. 
- 앞에서는 이해를 돕기 위해 비즈니스 로직을 호출한다는 표현 보다는, 비즈니스 로직이라 설명했다.


---

- 서블릿을 컨트롤러로 사용하고, JSP를 뷰로 사용해보자.
- 모델은 뭐에요?
    - 컨트롤러에서는 request.setAttribute로 값을 담고, 뷰에서는 request.getAttribute로 값을 가져올거에요.
    

```java
@WebServlet(name = "mvcMemberFormServlet", urlPatterns = "/servlet-mvc/members/new-form")
public class MvcMemberFormServlet extends HttpServlet { // 컨트롤러가 될 것이다.

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {

        // WAS Server rule : WEB-INF 폴더 내부에 있는 jsp들은, 외부 경로에서 호출해도 불러지지 않습니다!!!!!
        // http://localhost:8080/jsp/members/new-form.jsp 는 불러지지만
        // http://localhost:8080/WEB-INF/views/new-form.jsp 안불러집니다~
        // 항상 컨트롤러에서 거쳐서 호출되었으면 좋겠다? -> WEB-INF 경로에 넣어주면 됩니다.
        String viewPath = "/WEB-INF/views/new-form.jsp";

        final RequestDispatcher dispatcher = request.getRequestDispatcher(viewPath);// 컨트롤러에서 뷰로 이동할 때 씀

        // 다른 서블릿이나 JSP로 이동할 수 있는 기능이다. 서버 내부에서 다시 호출이 발생한다.
        // 그러니까 url이 바뀌지가 않는다. http://localhost:8080/servlet-mvc/members/new-form 요기에 들어가면 그냥 JSP 파일이 떠져있는 이유가 내부에서 호출했기 때문임
        // 사용자가 보기에는 URL은 JSP로 넘어가도 변하지 않습니다!
        dispatcher.forward(request, response); // 이걸 호출하면, 실제로 Controller -> view로 이동하게 됨 (서버 내부에서 서버끼리 호출하는 형태임)
    }
}
```
```html
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <meta charset="UTF-8">
  <title>Title</title>
</head>
<body>
<!-- 상대경로 사용, [현재 URL이 속한 계층 경로 + /save]  -->
<%-- 여기서 /save 로 쓰면 절대경로로 들어가는데 save처럼 그냥 상대경로를 쓰면 해당 디렉토리에서 save로 들어간다. --%>
<form action="save" method="post">
  username: <input type="text" name="username" />
  age: <input type="text" name="age" />
  <button type="submit">전송</button>
</form>
</body>
</html>
```

- 여기서 form의 action을 보면 절대 경로(로 시작)이 아니라 상대경로(로 시작X)하는 것을 확인할 수 있다.
- 이렇게 상대경로를 사용하면 폼 전송시 현재 URL이 속한 계층 경로 + save가 호출된다.
- 현재 계층 경로: /servlet-mvc/members/
- 결과: /servlet-mvc/members/save


> /WEB-INF
- 이 경로안에 JSP가 있으면 외부에서 직접 JSP를 호출할 수 없다. 
- 우리가 기대하는 것은 항상 컨트롤러를 통해서 JSP를 호출하는 것이다.

> redirect vs forward
- 리다이렉트는 실제 클라이언트(웹 브라우저)에 응답이 나갔다가, 클라이언트가 redirect 경로로 다시 요청한다.
- 따라서 클라이언트가 인지할 수 있고, URL 경로도 실제로 변경된다. 
- 반면에 포워드는 서버 내부에서 일어나는 호출이기 때문에 클라이언트가 전혀 인지하지 못한다.

---

### 회원 조회
```java
@WebServlet(name = "mvcMemberListServlet", urlPatterns = "/servlet-mvc/members")
public class MvcMemberListServlet extends HttpServlet {

    private MemberRepository memberRepository = MemberRepository.getInstance();

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {

        final List<Member> members = memberRepository.findAll();

        request.setAttribute("members", members);

        final String viewPath = "/WEB-INF/views/members.jsp";
        final RequestDispatcher dispatcher = request.getRequestDispatcher(viewPath);
        dispatcher.forward(request, response);
    }
}
```

members.jsp
```html
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><!-- JSTL 사용하기!! -->

<html>
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>

<body>
<a href="/index.html">메인</a>
<table>
    <thead>
    <th>id</th>
    <th>username</th>
    <th>age</th>
    </thead>
    <tbody>
    <c:forEach var="item" items="${members}">
        <tr>
            <td> ${item.id} </td>
            <td> ${item.username} </td>
            <td> ${item.age} </td>
        </tr>
    </c:forEach>
    </tbody>
</table>
</body>
</html>
```

MVC 패턴을 사용하니 컨트롤러와 뷰를 완전히 분리하게 되었음.
