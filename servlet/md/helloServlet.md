### `@ServletComponentScan` : 서블릿 자동 등록

- `@WebServlet` : 서블릿 애노테이션
    - name : 서블릿 이름
    - urlPatterns : URL 매핑

HTTP 요청을 통해 매핑된 URL이 호출되면, 서블릿 컨테이너는 다음 메서드를 실행한다.

```java
@Override
protected void service(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
    super.service(req, resp);
}
```

다음 정보를 보면, 요청 정보를 굉장히 자세히 볼 수 있다.

```
logging.level.org.apache.coyote.http11=debug
```

```text
Host: localhost:8080
Connection: keep-alive
Cache-Control: max-age=0
sec-ch-ua: " Not;A Brand";v="99", "Google Chrome";v="91", "Chromium";v="91"
sec-ch-ua-mobile: ?0
Upgrade-Insecure-Requests: 1
User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.114 Safari/537.36
Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9
Sec-Fetch-Site: none
Sec-Fetch-Mode: navigate
Sec-Fetch-User: ?1
Sec-Fetch-Dest: document
Accept-Encoding: gzip, deflate, br
Accept-Language: ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7
Cookie: Idea-b43838fa=853cb640-ca00-429d-9f95-20bed4ad0584; Idea-b4383cb9=0c2999ab-04af-44bc-b5c8-9460849bc684

]
HelloServlet.service
request = org.apache.catalina.connector.RequestFacade@1c74bafa
response = org.apache.catalina.connector.ResponseFacade@7fa2c329
username = 김
```

그러나, 운영 서버에 넣을 때는 성능 저하가 발생할 수 있다. 개발할 때만 적용하자.

> 참고

HTTP 응답에서 Content-Length는 웹 애플리케이션 서버가 자동으로 넣어준다.


---

HttpServletRequest 진입 지점 정보 출력

```java
private void printStartLine(HttpServletRequest request) {
    System.out.println("--- REQUEST-LINE - start ---");
    System.out.println("request.getMethod() = " + request.getMethod()); //GET
    System.out.println("request.getProtocal() = " + request.getProtocol()); // HTTP/1.1
    System.out.println("request.getScheme() = " + request.getScheme()); //http

    // http://localhost:8080/request-header
    System.out.println("request.getRequestURL() = " + request.getRequestURL());
    // /request-test
    System.out.println("request.getRequestURI() = " + request.getRequestURI());
    //username=hi
    System.out.println("request.getQueryString() = " + request.getQueryString());
    System.out.println("request.isSecure() = " + request.isSecure()); //https 사용 유무
    System.out.println("--- REQUEST-LINE - end ---");
    System.out.println();
}
```

Request Header의 모든 정보 출력

```java
//Header 모든 정보
private void printHeaders(HttpServletRequest request) {
    System.out.println("--- Headers - start ---");

//        Enumeration<String> headerNames = request.getHeaderNames();
//        while (headerNames.hasMoreElements()) {
//            String headerName = headerNames.nextElement();
//            System.out.println(headerName + ": " + request.getHeader(headerName));
//        }

    // request.getHeader("host") 와 같이 값을 바로 꺼내올 수도 있다!
    request.getHeaderNames().asIterator().forEachRemaining(headerName -> System.out.println(headerName + ": " + request.getHeader(headerName)));
    System.out.println("--- Headers - end ---");
    System.out.println();
}
```

Request Header의 편리한 출력

```java
//Header 편리한 조회
private void printHeaderUtils(HttpServletRequest request) {
    System.out.println("--- Header 편의 조회 start ---");

    System.out.println("[Host 편의 조회]");
    System.out.println("request.getServerName() = " + request.getServerName()); //Host 헤더
    System.out.println("request.getServerPort() = " + request.getServerPort()); //Host 헤더

    System.out.println();

    System.out.println("[Accept-Language 편의 조회]");
    request.getLocales().asIterator().forEachRemaining(locale -> System.out.println("locale = " + locale));
    System.out.println("request.getLocale() = " + request.getLocale());

    System.out.println();

    System.out.println("[cookie 편의 조회]");
    if (request.getCookies() != null) {
        for (Cookie cookie : request.getCookies()) {
            System.out.println(cookie.getName() + ": " + cookie.getValue());
        }
    }

    System.out.println();

    System.out.println("[Content 편의 조회]");
    System.out.println("request.getContentType() = " + request
            .getContentType()); // GET 방식은 request.getContentType() = null이 출력된다. postman으로 POST방식으로 문자열 하나 보내면 request.getContentType() = text/plain 가 출력됨
    System.out.println("request.getContentLength() = " + request.getContentLength());
    System.out.println("request.getCharacterEncoding() = " + request.getCharacterEncoding());

    System.out.println("--- Header 편의 조회 end ---");
    System.out.println();
}
```

기타 정보 출력

```java
//기타 정보
private void printEtc(HttpServletRequest request) {
    System.out.println("--- 기타 조회 start ---");
    System.out.println("[Remote 정보]");
    System.out.println("request.getRemoteHost() = " + request.getRemoteHost());
    System.out.println("request.getRemoteAddr() = " + request.getRemoteAddr());
    System.out.println("request.getRemotePort() = " + request.getRemotePort());
    System.out.println();
    System.out.println("[Local 정보]");
    System.out.println("request.getLocalName() = " + request.getLocalName());
    System.out.println("request.getLocalAddr() = " + request.getLocalAddr());
    System.out.println("request.getLocalPort() = " + request.getLocalPort());
    System.out.println("--- 기타 조회 end ---");
    System.out.println();
}
```

전체 코드

```java
@WebServlet(name = "requestHeaderServlet", urlPatterns = "/request-header")
public class RequestHeaderServlet extends HttpServlet {

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        printStartLine(request);
        printHeaders(request);
        printHeaderUtils(request);
        printEtc(request);
    }

    private void printStartLine(HttpServletRequest request) {
        System.out.println("--- REQUEST-LINE - start ---");
        System.out.println("request.getMethod() = " + request.getMethod()); //GET
        System.out.println("request.getProtocal() = " + request.getProtocol()); // HTTP/1.1
        System.out.println("request.getScheme() = " + request.getScheme()); //http

        // http://localhost:8080/request-header
        System.out.println("request.getRequestURL() = " + request.getRequestURL());
        // /request-test
        System.out.println("request.getRequestURI() = " + request.getRequestURI());
        //username=hi
        System.out.println("request.getQueryString() = " + request.getQueryString());
        System.out.println("request.isSecure() = " + request.isSecure()); //https 사용 유무
        System.out.println("--- REQUEST-LINE - end ---");
        System.out.println();
    }

    //Header 모든 정보
    private void printHeaders(HttpServletRequest request) {
        System.out.println("--- Headers - start ---");

//        Enumeration<String> headerNames = request.getHeaderNames();
//        while (headerNames.hasMoreElements()) {
//            String headerName = headerNames.nextElement();
//            System.out.println(headerName + ": " + request.getHeader(headerName));
//        }

        // request.getHeader("host") 와 같이 값을 바로 꺼내올 수도 있다!
        request.getHeaderNames().asIterator().forEachRemaining(headerName -> System.out.println(headerName + ": " + request.getHeader(headerName)));
        System.out.println("--- Headers - end ---");
        System.out.println();
    }

    //Header 편리한 조회
    private void printHeaderUtils(HttpServletRequest request) {
        System.out.println("--- Header 편의 조회 start ---");

        System.out.println("[Host 편의 조회]");
        System.out.println("request.getServerName() = " + request.getServerName()); //Host 헤더
        System.out.println("request.getServerPort() = " + request.getServerPort()); //Host 헤더

        System.out.println();

        System.out.println("[Accept-Language 편의 조회]");
        request.getLocales().asIterator().forEachRemaining(locale -> System.out.println("locale = " + locale));
        System.out.println("request.getLocale() = " + request.getLocale());

        System.out.println();

        System.out.println("[cookie 편의 조회]");
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                System.out.println(cookie.getName() + ": " + cookie.getValue());
            }
        }

        System.out.println();

        System.out.println("[Content 편의 조회]");
        System.out.println("request.getContentType() = " + request
                .getContentType()); // GET 방식은 request.getContentType() = null이 출력된다. postman으로 POST방식으로 문자열 하나 보내면 request.getContentType() = text/plain 가 출력됨
        System.out.println("request.getContentLength() = " + request.getContentLength());
        System.out.println("request.getCharacterEncoding() = " + request.getCharacterEncoding());

        System.out.println("--- Header 편의 조회 end ---");
        System.out.println();
    }

    //기타 정보
    private void printEtc(HttpServletRequest request) {
        System.out.println("--- 기타 조회 start ---");
        System.out.println("[Remote 정보]");
        System.out.println("request.getRemoteHost() = " + request.getRemoteHost());
        System.out.println("request.getRemoteAddr() = " + request.getRemoteAddr());
        System.out.println("request.getRemotePort() = " + request.getRemotePort());
        System.out.println();
        System.out.println("[Local 정보]");
        System.out.println("request.getLocalName() = " + request.getLocalName());
        System.out.println("request.getLocalAddr() = " + request.getLocalAddr());
        System.out.println("request.getLocalPort() = " + request.getLocalPort());
        System.out.println("--- 기타 조회 end ---");
        System.out.println();
    }
}
```
