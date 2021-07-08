package hello.servlet.basic.response;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "responseHeaderServlet", urlPatterns = "/response-header")
public class ResponseHeaderServlet extends HttpServlet {
    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {

        // [status-line]
        response.setStatus(HttpServletResponse.SC_OK); // HTTP 응답 코드 지정

        // [response-headers]
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // 캐시 완전 무효화
        response.setHeader("Pragma", "no-cache"); // 과거 캐시 이력까지 지움 (완전 캐시 무효화를 위해서는 윗 라인과 같이 작성해주면 된다.)
        response.setHeader("my-header", "hello");

        // [Header 편의 메서드]
        content(response);
        cookie(response);
        redirect(response);

        // [Message body]
        response.getWriter().write("ok");
    }

    private void redirect(final HttpServletResponse response) throws IOException {
        // Status Code 302
        // Location : /basic/hello-form.html

//        response.setStatus(HttpServletResponse.SC_FOUND); //302
//        response.setHeader("Location", "/basic/hello-form.html");

        // 다음 코드는 위의 2줄과 동일하다.
         response.sendRedirect("/basic/hello-form.html");
    }

    private void content(final HttpServletResponse response) {
//        response.setHeader("Content-Type","text/plain;charset=utf-8");
        // 이는 위와 동일하다.
        response.setContentType("text/plain");
        response.setCharacterEncoding("utf-8");
//        response.setContentLength(2); // 생략시 자동 생성
    }

    private void cookie(HttpServletResponse response) {
        // Set-Cookie : myCookie = good; Max-Age=600;
//        response.setHeader("Set-Cookie","myCookie=good; Max-Age=600");

        // 이는 위와 동일하다
        final Cookie cookie = new Cookie("myCookie", "good");
        cookie.setMaxAge(600); // 600 초
        response.addCookie(cookie);
    }

}
