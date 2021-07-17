<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
성공
<ul>
    <li>
        id = ${member.id}
        username = ${member.username}
        age = ${member.age}
        <%-- property 접근법 : getAge같이 사용하지 말고 .property로 접근. JSP에서 제공하는 것임  --%>
    </li>
</ul>
<a href="/index.html">메인</a>
</body>
</html>
