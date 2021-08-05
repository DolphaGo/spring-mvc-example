package hello.servlet.web.springmvc.v1;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

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
