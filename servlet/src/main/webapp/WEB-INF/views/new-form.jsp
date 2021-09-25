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
