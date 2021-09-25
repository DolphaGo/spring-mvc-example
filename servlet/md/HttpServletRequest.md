### HttpServletRequest 개요

> HttpServletRequest 역할

- HTTP 요청 메시지를 개발자가 직접 파싱해서 사용해도 되지만, 매우 불편할 것
- 서블릿은 개발자가 HTTP 요청 메시지를 편리하게 사용할 수 있도록 개발자 대신에 HTTP 요청 메시지를 파싱한다.
- 그리고 그 결과를 `HttpServletRequest` 객체에 담아서 제공한다.

HttpServletRequest를 사용하면 다음과 같은 HTTP 요청 메시지를 편리하게 조회할 수 있다.

```text
POST /save HTTP/1.1
Host: localhost:8080
Content-Type: application/x-www-form-urlencoded

username=kim&age=20
```

- Start LINE
    - HTTP 메서드
    - URL
    - Querystring
    - scheme, protocol
- Header
    - Header 조회
- Body
    - form 파라미터 형식 조회(username=kim&age=20와 같이 body로 넘어오면 request.getParameter("username")으로 쉽게 읽을 수 있다.)
    - message body 데이터 직접 조회

HttpServletRequest 객체는 추가로 여러가지 부가 기능도 함께 제공한다.

`임시 저장소 기능`

- 해당 HTTP 요청이 시작부터 끝날 때까지 유지되는 **임시 저장소 기능**
    - 저장 : `request.setAttribute(name, value)`
    - 조회 : `request.getAttribute(name)`

`세션 관리 기능`

- `request.getSession(create: true)`

중요한 것

- HttpServletRequest, HttpServletResponse 를 사용할 때 가장 중요한 점은 이 객체들이 HTTP 요청 메시지, HTTP 응답 메시지를 편리하게 사용하도록 도와주는 객체라는 점.
- 따라서 이 기능을 깊이 이해하려면 HTTP 스펙이 제공하는 요청, 응답 메시지 자체를 이해해야 한다.






