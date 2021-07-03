### `@ServletComponentScan` : 서블릿 자동 등록

- `@WebServlet` : 서블릿 애노테이션
    - name : 서블릿 이름
    - urlPatterns : URL 매핑
    

HTTP 요청을 통해 매핑된 URL이 호출되면, 서블릿 컨테이너는 다음 메서드를 실행한다.
```java
public void service(final ServletRequest request, final ServletResponse response) throws ServletException, IOException {
    ...
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


