package hello.servlet.web.frontcontroller.v1.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hello.servlet.web.frontcontroller.v1.ControllerV1;

public class MemberFormControllerV1 implements ControllerV1 {

    @Override
    public void process(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        String viewPath = "/WEB-INF/views/new-form.jsp";
        final RequestDispatcher dispatcher = request.getRequestDispatcher(viewPath);// 컨트롤러에서 뷰로 이동할 때 씀
        dispatcher.forward(request, response); // 이걸 호출하면, 실제로 Controller -> view로 이동하게 됨 (서버 내부에서 서버끼리 호출하는 형태임)
    }
}
