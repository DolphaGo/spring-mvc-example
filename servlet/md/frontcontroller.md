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

