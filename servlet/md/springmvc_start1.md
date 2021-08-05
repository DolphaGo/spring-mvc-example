`@Controller`

- 컴포넌트 스캔의 대상이 됨 ( 스프링이 자동으로 스프링 빈으로 등록한다. )
- 스프링 MVC에서 애노테이션 기반 컨트롤러로 인식한다.

`@RequestMapping`

- 요청 정보를 매핑한다. 해당 URL이 호출되면 이 메서드가 호출된다.

`RequestMappingHandlerMapping`은 스프링 빈 중에서 @RequestMapping 또는 @Controller 가 클래스 레벨에 붙어있는 경우 매핑 정보로 인식한다.

- Save

````java

@Controller
public class SpringMemberSaveControllerV1 {
    private MemberRepository memberRepository = MemberRepository.getInstance();

    @RequestMapping("/springmvc/v1/members/save")
    public ModelAndView process(HttpServletRequest request, HttpServletResponse response) {
        final String username = request.getParameter("username");
        final int age = Integer.parseInt(request.getParameter("age"));

        final Member member = new Member(username, age);
        memberRepository.save(member);

        final ModelAndView mv = new ModelAndView("save-result");
        mv.addObject("member", member);
        return mv;
    }

}
````

- List

````java

@Controller
public class SpringMemberListControllerV1 {
    private MemberRepository memberRepository = MemberRepository.getInstance();

    @RequestMapping("/springmvc/v1/members")
    public ModelAndView process() {
        final List<Member> members = memberRepository.findAll();
        final ModelAndView mv = new ModelAndView("members");
        mv.addObject("members", members);
        return mv;
    }
}
````

- Form

````java

@Controller
// 아래 2개의 어노테이션은 @Controller와 동일하다.
//@RequestMapping : 클래스 레벨에 있어야한다. RequestMappingHandlerMapping.isHandler를 보면 알 수 있다.
//@Component
public class SpringMemberFormControllerV1 {

    @RequestMapping("/springmvc/v1/members/new-form")
    public ModelAndView process() {
        /**
         * spring.mvc.view.prefix=/WEB-INF/views/
         * spring.mvc.view.suffix=.jsp
         */
        return new ModelAndView("new-form");
    }
}
````

- mv.addObject("member", member)
  - 스프링이 제공하는 ModelAndView 를 통해 Model 데이터를 추가할 때는 addObject() 를 사용하면 된다. 
  - 이 데이터는 이후 뷰를 렌더링 할 때 사용된다.
