package hello.servlet.web.springmvc.v3;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import hello.servlet.domain.member.Member;
import hello.servlet.domain.member.MemberRepository;

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
