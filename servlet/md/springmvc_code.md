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

이렇게 여러개의 컨트롤러를 통합할 수도 있다.

```java

@Controller
public class SpringMemberControllerV2 {
    private MemberRepository memberRepository = MemberRepository.getInstance();

    @RequestMapping("/springmvc/v2/members/new-form")
    public ModelAndView newForm() {
        /**
         * spring.mvc.view.prefix=/WEB-INF/views/
         * spring.mvc.view.suffix=.jsp
         */
        return new ModelAndView("new-form");
    }

    @RequestMapping("/springmvc/v2/members")
    public ModelAndView members() {
        final List<Member> members = memberRepository.findAll();
        final ModelAndView mv = new ModelAndView("members");
        mv.addObject("members", members);
        return mv;
    }

    @RequestMapping("/springmvc/v2/members/save")
    public ModelAndView save(HttpServletRequest request, HttpServletResponse response) {
        final String username = request.getParameter("username");
        final int age = Integer.parseInt(request.getParameter("age"));

        final Member member = new Member(username, age);
        memberRepository.save(member);

        final ModelAndView mv = new ModelAndView("save-result");
        mv.addObject("member", member);
        return mv;
    }
}
```

이 중에서도 중복이 존재한다. ("/springmvc/v2/members")
합쳐보자.

```java

@Controller
@RequestMapping("/springmvc/v2/members")
public class SpringMemberControllerV2 {
    private MemberRepository memberRepository = MemberRepository.getInstance();

    @RequestMapping("/new-form")
    public ModelAndView newForm() {
        /**
         * spring.mvc.view.prefix=/WEB-INF/views/
         * spring.mvc.view.suffix=.jsp
         */
        return new ModelAndView("new-form");
    }

    @RequestMapping
    public ModelAndView members() {
        final List<Member> members = memberRepository.findAll();
        final ModelAndView mv = new ModelAndView("members");
        mv.addObject("members", members);
        return mv;
    }

    @RequestMapping("/save")
    public ModelAndView save(HttpServletRequest request, HttpServletResponse response) {
        final String username = request.getParameter("username");
        final int age = Integer.parseInt(request.getParameter("age"));

        final Member member = new Member(username, age);
        memberRepository.save(member);

        final ModelAndView mv = new ModelAndView("save-result");
        mv.addObject("member", member);
        return mv;
    }
}
```

> 조합

- 컨트롤러 클래스를 통합하는 것을 넘어서 조합도 가능하다.
- 다음 코드는 /springmvc/v2/members 라는 부분에 중복이 있다.

- `@RequestMapping("/springmvc/v2/members/new-form")`
- `@RequestMapping("/springmvc/v2/members")`
- `@RequestMapping("/springmvc/v2/members/save")`

- 물론 이렇게 사용해도 되지만, 컨트롤러를 통합한 예제 코드를 보면 중복을 어떻게 제거했는지 확인할 수 있다.
- 클래스 레벨에 다음과 같이 @RequestMapping 을 두면 메서드 레벨과 조합이 된다.

 ```java
@Controller
@RequestMapping("/springmvc/v2/members")
public class SpringMemberControllerV2 {}
```
- 조합 결과 클래스 레벨 @RequestMapping("/springmvc/v2/members")
- 메서드 레벨 @RequestMapping("/new-form") /springmvc/v2/members/new-form 
- 메서드 레벨 @RequestMapping("/save") /springmvc/v2/members/save 
- 메서드 레벨 @RequestMapping /springmvc/v2/members

가 된다!!


이걸 더 실용적인 컨트롤러로 바꿀 수는 없을까?
Annotation 기반의 컨트롤러는 다양한 기능을 지원한다.
- 모델, HttpRequest의 Parameter, ModelAndView 대신 String으로 리턴해도 자동으로 View를 찾아준다.
```java
@Controller
@RequestMapping("/springmvc/v3/members")
public class SpringMemberControllerV3 {

    private MemberRepository memberRepository = MemberRepository.getInstance();

    @RequestMapping("/new-form")
    public String newForm() {
        return "new-form"; // 스프링 애노테이션 기반 컨트롤러는 ModelAndView를 반환해도 되고, 문자열로 반환해도 된다. 어째뜬 ModelAndView로 움직임.
    }

    @RequestMapping
    public String members(Model model) {
        final List<Member> members = memberRepository.findAll();
        model.addAttribute("members", members);
        return "members";
    }

    @RequestMapping("/save")
    public String save(
            @RequestParam("username") String username,
            @RequestParam("age") int age,
            Model model) {
        //HttpServletRequest request, HttpServletResponse response 대신 RequestParam도 받을 수 있다. 타입 캐스팅이나 타입 변환도 자동으로 지원해준다.
        //또한 모델도 지원하여, 기존 V2코드에 비해 굉장히 간결해진 것을 볼 수 있다.

        final Member member = new Member(username, age);
        memberRepository.save(member);

        model.addAttribute("member", member);
        return "save-result";
    }
}
```

- 지금까지는 GET으로 오든, POST로 오든, PUT으로 오든, 경로만 맞으면 해당 요청이 동작하도록 했다.
- HTTP Method를 추가해보자.
```java
@Controller
@RequestMapping("/springmvc/v3/members")
public class SpringMemberControllerV3_1 {

    private MemberRepository memberRepository = MemberRepository.getInstance();

    @GetMapping("/new-form")
    public String newForm() {
        return "new-form"; // 스프링 애노테이션 기반 컨트롤러는 ModelAndView를 반환해도 되고, 문자열로 반환해도 된다. 어째뜬 ModelAndView로 움직임.
    }

    @GetMapping
    public String members(Model model) {
        final List<Member> members = memberRepository.findAll();
        model.addAttribute("members", members);
        return "members";
    }

    @PostMapping("/save")
    public String save(
            @RequestParam("username") String username,
            @RequestParam("age") int age,
            Model model) {
        //HttpServletRequest request, HttpServletResponse response 대신 RequestParam도 받을 수 있다. 타입 캐스팅이나 타입 변환도 자동으로 지원해준다.
        //또한 모델도 지원하여, 기존 V2코드에 비해 굉장히 간결해진 것을 볼 수 있다.

        final Member member = new Member(username, age);
        memberRepository.save(member);

        model.addAttribute("member", member);
        return "save-result";
    }
}
```
