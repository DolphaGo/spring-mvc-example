package hello.springmvc.basic.response;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ResponseViewController {

    @RequestMapping("/response-view-v1")
    public ModelAndView responseViewV1() {
        final ModelAndView mav = new ModelAndView("response/hello")
                .addObject("data", "hello!");
        return mav;
    }

    @RequestMapping("/response-view-v2")
    public String responseViewV2(Model model) {
        model.addAttribute("data", "hello?");
        return "response/hello"; // @Controller 안에서 String을 반환하면, 이 String은 view의 논리적인 이름이 된다.
    }

    @ResponseBody
    @RequestMapping("/response-view-v2_1")
    public String responseViewV2_1(Model model) {
        model.addAttribute("data", "hello?");
        return "response/hello"; //@ResponseBody를 붙이면 이 문자가 그대로 http응답으로 나가버리게 된다.
    }

    // 별로 추천하진 않음
    @RequestMapping("/response/hello")
    public void responseViewV3(Model model) {
        model.addAttribute("data", "hello!!"); // @RequestMapping("/response/hello")의 경로와 응답의 뷰가 동일하다면 동일한 논리적 뷰를 리턴함
    }
}
