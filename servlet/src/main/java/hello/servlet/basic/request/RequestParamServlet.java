package hello.servlet.basic.request;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 1. 파라미터 전송 기능
 * http://localhost:8080/request-param?username=hello&age=20
 */
@WebServlet(name = "requestParamServlet", urlPatterns = "/request-param")
public class RequestParamServlet extends HttpServlet {
    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {

        // http://localhost:8080/request-param?username=dolphago&age=20
        System.out.println("[전체 파라미터 조회] - start");

        // username=dolphago
        // age=20
        request.getParameterNames().asIterator()
               .forEachRemaining(paramName -> System.out.println(paramName + "=" + request.getParameter(paramName)));

        System.out.println("[전체 파라미터 조회] - end");

        System.out.println("[단일 파라미터 조회] - start");

        final String username = request.getParameter("username");
        final String age = request.getParameter("age");

        System.out.println("username = " + username);
        System.out.println("age = " + age);

        System.out.println("[단일 파라미터 조회] - end");

        // 만약 다음과 같이 같은 파라미터에 2개 이상의 데이터가 들어온다면? 가장 먼저 매칭된 dolphago먼저 출력된다.
        // http://localhost:8080/request-param?username=dolphago&age=20&username=dolphago2
        // 결과
        // [전체 파라미터 조회] - start
        // username=dolphago
        // age=20
        // [전체 파라미터 조회] - end
        // [단일 파라미터 조회] - start
        // username = dolphago
        // age = 20
        // [단일 파라미터 조회] - end

        // 하지만 이런 것들도 중복된 것을 찾아볼 수 있는 방법도 있다.
        System.out.println("[이름이 같은 복수 파라미터 조회]");
        final String[] usernames = request.getParameterValues("username");
        for (String name : usernames) {
            System.out.println("name = " + name);
        }
        // 결과
        // [이름이 같은 복수 파라미터 조회]
        // name = dolphago
        // name = dolphago2

        response.getWriter().write("ok");
    }
}
