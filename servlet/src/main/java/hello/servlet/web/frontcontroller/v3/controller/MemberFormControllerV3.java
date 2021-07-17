package hello.servlet.web.frontcontroller.v3.controller;

import java.util.Map;

import hello.servlet.web.frontcontroller.ModelView;
import hello.servlet.web.frontcontroller.v3.ControllerV3;

public class MemberFormControllerV3 implements ControllerV3 {
    @Override
    public ModelView process(final Map<String, String> paramMap) {
        return new ModelView("new-form"); // WEB-INF/views 와 .jsp를 뺀 논리명만
    }
}
