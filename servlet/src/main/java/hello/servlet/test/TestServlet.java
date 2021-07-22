package hello.servlet.test;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "testServlet", urlPatterns = "/test")
public class TestServlet extends HttpServlet {

    @Override
    protected void service(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("Service 1 In = " + Thread.currentThread());
        try {
            Thread.currentThread().sleep(15000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("complete Test 1");
    }
}
