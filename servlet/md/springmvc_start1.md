`@Controller`
- 컴포넌트 스캔의 대상이 됨 ( 스프링이 자동으로 스프링 빈으로 등록한다. )
- 스프링 MVC에서 애노테이션 기반 컨트롤러로 인식한다.


`@RequestMapping`
- 요청 정보를 매핑한다. 해당 URL이 호출되면 이 메서드가 호출된다.


`RequestMappingHandlerMapping`은 스프링 빈 중에서 @RequestMapping 또는 @Controller 가 클래스 레벨에 붙어있는 경우 매핑 정보로 인식한다.


