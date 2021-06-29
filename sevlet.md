### 서블릿



서버에서 처리해야 하는 업무

- 서버 TCP/IP 연결 대기, 소켓 연결
- POST 방식, /save URL 파악
- Content-Type 확인
- HTTP 메시지 바디 내용 파싱
  - username, age 데이터를 사용할 수 있게 파싱
- 저장 프로세스 실행
- **비즈니스 로직 실행**
  - **데이터베이스에 저장 요청**
- HTTP 응답 메세지 생성 시작
  - HTTP 시작 라인 생성
  - Header 생성
  - 메시지 바디에 HTML 생성에서 입력
- TCP/IP에 응답 전달, 소켓 종료





비즈니스 로직 실행 전/후로 해야할 것들이 너무나도 많다.

이러한 모든 것들을 지원해주는 **서블릿**등장.



> **서블릿**

- urlPatterns(/hello)의 URL이 호출되면 서블릿 코드가 실행
- HTTP 요청 정보를 편리하게 사용할 수 있는 HttpServletRequest
- HTTP 응답 정보를 편리하게 사용할 수 있는 HttpServletResponse
- 개발자는 HTTP 스펙을 매우 편리하게 사용할 수 있게 된다!

```java
@WebServlet(name = "helloServlet", urlPatterns = "/hello")
public class HelloServlet extends HttpServlet {
  @Override
  protected void service(HttpServletRequest request, HttpServletResponse response){
    // Application Logic
  }
}
```





- WAS 안에는 서블릿 컨테이너가 있다.

- 서블릿 객체를 생성해주고 자동으로 호출도 해줌, 생명주기도 관리해줌



### 서블릿

#### 서블릿 컨테이너

- 톰캣처럼 서블릿을 지원하는 WAS를 서블릿 컨테이너라고 한다.
- 서블릿 컨테이너는 서블릿 객체를 생성, 초기화, 호출, 종료하는 생명주기 관리
- 서블릿 객체는 **싱글톤으로 관리!!**
  - request나 response는 요청이 올 때마다 새로 생성되는 것은 맞다.
  - 그러나 위의 예제에서 `helloServlet` 을 계속 만드는 것이 의미가 있을까?
  - 고객의 요청이 올 때 마다 계속 객체를 생성하는 것은 비효율적이기 때문
  - 최초 로딩 시점에 서블릿 객체를 미리 만들어두고 **재활용**
  - 모든 고객 요청은 동일한 서브릿 객체 인스턴스에 접근
  - **공유 변수 사용 주의할 것!!**
  - 서블릿 컨테이너 종료시 함께 종료됨(생명 주기 관리도 같이 해준다~)
- JSP도 서블릿으로 변환되어서 사용된다.
- 동시 요청을 위한 멀티 쓰레드 처리 지원도 해준다!