package hello.servlet.web.servletmvc;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "mvcMemberFormServlet", urlPatterns = "/servlet-mvc/members/new-form")
public class MvcMemberFormServlet extends HttpServlet { // 컨트롤러가 될 것이다.

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {

        // WAS Server rule : WEB-INF 폴더 내부에 있는 jsp들은, 외부 경로에서 호출해도 불러지지 않습니다!!!!!
        // http://localhost:8080/jsp/members/new-form.jsp 는 불러지지만
        // http://localhost:8080/WEB-INF/views/new-form.jsp 안불러집니다~
        // 항상 컨트롤러에서 거쳐서 호출되었으면 좋겠다? -> WEB-INF 경로에 넣어주면 됩니다.
        String viewPath = "/WEB-INF/views/new-form.jsp";

        final RequestDispatcher dispatcher = request.getRequestDispatcher(viewPath);// 컨트롤러에서 뷰로 이동할 때 씀

        // 다른 서블릿이나 JSP로 이동할 수 있는 기능이다. 서버 내부에서 다시 호출이 발생한다.
        // 그러니까 url이 바뀌지가 않는다. http://localhost:8080/servlet-mvc/members/new-form 요기에 들어가면 그냥 JSP 파일이 떠져있는 이유가 내부에서 호출했기 때문임
        // 사용자가 보기에는 URL은 JSP로 넘어가도 변하지 않습니다!
        dispatcher.forward(request, response); // 이걸 호출하면, 실제로 Controller -> view로 이동하게 됨 (서버 내부에서 서버끼리 호출하는 형태임)
    }
}
