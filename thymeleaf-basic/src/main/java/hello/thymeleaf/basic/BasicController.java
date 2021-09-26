package hello.thymeleaf.basic;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.Data;

@Controller
@RequestMapping("/basic")
public class BasicController {

    @GetMapping("/text-basic")
    public String textBasic(Model model) {
        model.addAttribute("data", "<b>Hello Spring!</b>");
        // <b>Hello Spring!</b> 라고 작성하게 되면, 렌더링이 안됨, 렌더링을 해보면 &lt;b&gt;Hello Spring!&lt;/b&gt; 와 같은 문자로 됨
        return "basic/text-basic";
    }

    @GetMapping("/text-unescaped")
    public String textUnEscaped(Model model) {
        model.addAttribute("data", "<b>Hello Spring!</b>");
        // <b>Hello Spring!</b> 라고 작성하게 되면, 렌더링이 안됨, 렌더링을 해보면 &lt;b&gt;Hello Spring!&lt;/b&gt; 와 같은 문자로 됨
        // 이러한 특수 문자를 HTML Entity라고 합니다. 웹 브라우저는 `<` 를 태그의 시작으로 인식합니다.
        // HTML에서 사용하는 특수 문자를 HTML 엔티티 언어로 변환하는 것을 HTML Escape라고 합니다.
        // 이스케이프를 사용하지 않으려면 어떻게 해야할 까?
        // 타임리프는 2가지 기능을 제공한다.
        // th:text(이스케이프) -> th:utext (언이스케이프) 로 변환
        // [[...]] -> [(...)] 로 사용
        // 결과 : <b>Hello Spring!</b>
        return "basic/text-basic-unescaped";
    }

    /**
     * escaped 처리가 기본인 이유
     * 뭐 제목에도 < 와 같은 특수 문자를 쓰고, 등등
     * 사용상에 자유로움을 주는데, 이걸 다 unescaped 처리하면 화면이 다 깨지게 된다 (실전 경험)
     * 따라서, 기본적으로 escaped 처리가 되어 있는 것이다.
     */

    @GetMapping("/variable")
    public String variable(Model model) {
        final User userA = new User("userA", 10);
        final User userB = new User("userB", 20);

        final List<User> list = new ArrayList<>();
        list.add(userA);
        list.add(userB);

        final Map<String, User> map = new HashMap<>();

        map.put("userA", userA);
        map.put("userB", userB);

        model.addAttribute("user", userA);
        model.addAttribute("users", list);
        model.addAttribute("userMap", map);

        return "basic/variable";
    }

    @GetMapping("/basic-objects")
    public String basicObjects(HttpSession session) {
        session.setAttribute("sessionData", "Hello Session");
        return "basic/basic-objects";
    }

    @GetMapping("/date")
    public String date(Model model) {
        model.addAttribute("localDateTime", LocalDateTime.now());
        return "basic/date";
    }

    @GetMapping("/link")
    public String link(Model model) {
        model.addAttribute("param1", "data1");
        model.addAttribute("param2", "data2");
        return "basic/link";
    }

    @Data
    static class User {
        private String username;
        private int age;

        public User(final String username, final int age) {
            this.username = username;
            this.age = age;
        }
    }

    @Component("helloBean")
    static class HelloBean {
        public String hello(String data) {
            return "Hello " + data;
        }
    }
}
