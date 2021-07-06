package hello.servlet.basic;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "helloServlet", urlPatterns = "/hello")
public class HelloServlet extends HttpServlet {

    // public service : 톰캣에서 호출
    // 필요한 몇가지 작업 이후 protected service 호출
    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        System.out.println("HelloServlet.service");

        // 다양한 WAS들이 각각 서블릿 표준 스펙을 구현했을 텐데 그 구현체들임
        System.out.println("request = " + request); // request = org.apache.catalina.connector.RequestFacade@6919090c
        System.out.println("response = " + response); // response = org.apache.catalina.connector.ResponseFacade@523c01c

        // http://localhost:8080/hello?username=kim 처럼 요청을 보낸다면? (쿼리 파라미터를 가진 요청)
        final String username = request.getParameter("username");
        System.out.println("username = " + username); // 편리하게 queryParam을 가져올 수 있다.(파싱을 다 해준다~)

        // 응답은 response에 넣어주면 되겠죠? http 응답 메세지에 담아 보내기 위해 response에 값을 넣어주면 된다.
        response.setContentType("text/plain");
        response.setCharacterEncoding("utf-8");
        response.getWriter().write("hello " + username);
    }
}
