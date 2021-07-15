## JSP

`<%@ page contentType="text/html;charset=UTF-8" language="java" %>`
- 첫 줄은 JSP문서라는 뜻이다. JSP 문서는 이렇게 시작해야 한다.


- 회원 등록 폼 JSP를 보면 첫 줄을 제외하고는 완전히 HTML와 똑같다. 
- JSP는 서버 내부에서 서블릿으로 변환되는데, 우리가 만들었던 MemberFormServlet과 거의 비슷한 모습으로 변환된다.

> 실행
- `http://localhost:8080/jsp/members/new-form.jsp` 실행시 .jsp 까지 함께 적어주어야 한다.

JSP는 자바 코드를 그대로 다 사용할 수 있다.

#### <%@ page import="hello.servlet.domain.member.MemberRepository" %> : 자바의 import 문과 같다.
#### <% ~~ %> : 이 부분에는 자바 코드를 입력할 수 있다.
#### <%= ~~ %> : 이 부분에는 자바 코드를 출력할 수 있다.

- 회원 저장 JSP를 보면, 회원 저장 서블릿 코드와 같다. 
- 다른 점이 있다면, HTML을 중심으로 하고, 자바 코드를 부분부분 입력해주었다.
- <% ~ %> 를 사용해서 HTML 중간에 자바 코드를 출력하고 있다.


---

## 서블릿과 JSP의 한계

- 서블릿으로 개발할 때는 뷰(View)화면을 위한 HTML을 만드는 작업이 자바 코드에 섞여서 지저분하고 복잡했다.
- JSP를 사용한 덕분에 뷰를 생성하는 HTML 작업을 깔끔하게 가져가고, 중간중간 동적으로 변경이 필요한 부분에만 자바 코드를 적용했다. 
- 그런데 이렇게 해도 해결되지 않는 몇가지 고민이 남는다.

- 회원 저장 JSP를 보자. 
```java
<%@ page import="hello.servlet.domain.member.Member" %>
<%@ page import="hello.servlet.domain.member.MemberRepository" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    // request, response는 그냥 사용 가능하다.
    MemberRepository memberRepository = MemberRepository.getInstance();

    System.out.println("MemberSaveServlet.service");
    final String username = request.getParameter("username");
    final int age = Integer.parseInt(request.getParameter("age"));

    final Member member = new Member(username, age);
    memberRepository.save(member);

%>
<html>
<head>
    <title>Title</title>
</head>
<body>
성공
<ul>
    <li>
        id = <%= member.getId()%>
        username = <%= member.getUsername()%>
        age = <%= member.getAge()%>
    </li>
</ul>
<a href="/index.html">메인</a>
</body>
</html>
```
- 코드의 상위 절반은 회원을 저장하기 위한 비즈니스 로직이고, 나머지 하위 절반만 결과를 HTML로 보여주기 위한 뷰 영역이다. 
- 회원 목록의 경우에도 마찬가지다.
```java
<%@ page import="hello.servlet.domain.member.MemberRepository" %>
<%@ page import="hello.servlet.domain.member.Member" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    MemberRepository memberRepository = MemberRepository.getInstance();
    final List<Member> members = memberRepository.findAll();
%>
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
    <%
        for (Member member : members) {
            out.write("    <tr>");
            out.write("      <td>" + member.getId() + "</td>");
            out.write("      <td>" + member.getUsername() + "</td>");
            out.write("      <td>" + member.getAge() + "</td>");
            out.write("    </tr>");
        }
    %>
    </tbody>
</table>
</body>
</html>
```

- 코드를 잘 보면, JAVA 코드, 데이터를 조회하는 리포지토리 등등 다양한 코드가 모두 JSP에 노출되어 있다. JSP가 너무 많은 역할을 한다. 이렇게 작은 프로젝트도 벌써 머리가 아파오는데, 수백 수천줄이 넘어가는 JSP를 떠올려보면 정말 지옥과 같을 것이다. (유지보수 지옥 썰)


### MVC 패턴의 등장
- 비즈니스 로직은 서블릿 처럼 다른곳에서 처리하고, JSP는 목적에 맞게 HTML로 화면(View)을 그리는 일에 집중하도록 하자. 
- 과거 개발자들도 모두 비슷한 고민이 있었고, 그래서 MVC 패턴이 등장했다. 
- 우리도 직접 MVC 패턴을 적용해서 프로젝트를 리팩터링 해보자.
