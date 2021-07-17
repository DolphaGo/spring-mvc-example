package hello.servlet.web.frontcontroller.v3;

import java.util.Map;

import hello.servlet.web.frontcontroller.ModelView;

public interface ControllerV3 {
    ModelView process(Map<String, String> paramMap); // 서블릿 기술이 하나도 없다!(서블릿에 종속적이지 않다!!)
}
