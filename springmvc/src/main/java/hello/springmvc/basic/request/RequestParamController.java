package hello.springmvc.basic.request;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import hello.springmvc.basic.HelloData;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class RequestParamController {

    @RequestMapping("/request-param-v1")
    public void requestParamV1(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String username = request.getParameter("username");
        final int age = Integer.parseInt(request.getParameter("age"));
        log.info("username = {}, age = {}", username, age);
        response.getWriter().write("ok"); // void 리턴형이지만, response에 값을 써버리면 값 쓴 것을 그대로 보여준다.
    }

    @ResponseBody // View-Resolver로 찾아가지 않도록, RestController와 같은 효과를 내도록 한다.
    @RequestMapping("/request-param-v2")
    public String requestParamV2(
            @RequestParam("username") String memberName,
            @RequestParam("age") int memberAge
    ) {
        log.info("username = {}, age = {}", memberName, memberAge);
        return "ok";
    }

    @ResponseBody
    @RequestMapping("/request-param-v3")
    public String requestParamV3(@RequestParam String username, @RequestParam int age) { // 변수명이 동일하다면 RequestParam으로 받는 변수명을 생략할 수 있다.
        log.info("username = {}, age = {}", username, age);
        return "ok";
    }

    @ResponseBody
    @RequestMapping("/request-param-v4")
    public String requestParamV4(String username, int age) { // 인간의 욕심은 끝이 없지, @RequestParam 조차 생략할 수 있다. (String, int, Integer와 같은 단순타입이면 @RequestParam도 생략가능)
        log.info("username = {}, age = {}", username, age); // 하지만 너무 없는 것도 좀 과하다. @RequestParam이 있으면 명확하게 요청 파라미터에서 데이터를 읽는다는 것을 알 수 있음.
        return "ok";
    }

    @ResponseBody
    @RequestMapping("/request-param-required")
    public String requestParamRequired(
            @RequestParam(required = true) String username, // username=""로 들어오는 것도 통과가 됨! 즉, 요청을 `username=` 이렇게만 보낸 경우에도 통과가 된다.
            @RequestParam(required = false) Integer age) { // int age는 null이 될 수 없음. 그래서 age가 없어도 에러가 남. 이런 에러를 방지하려면 Integer로 바꾸면 됨
//        int a = null; //에러
//        Integer a = null;// ok

        /**
         * http://localhost:8080/request-param-required?username=
         * 결과 : username = , age = null
         */
        log.info("username = {}, age = {}", username, age);
        return "ok";
    }

    /**
     * defaultValue가 들어가게 되면, 값이 없을 때(null)만 적용되기 때문에, required가 사실상 의미가 없게 된다.
     */
    @ResponseBody
    @RequestMapping("/request-param-default")
    public String requestParamDefault(
            @RequestParam(defaultValue = "guest") String username,
            @RequestParam(defaultValue = "-1") int age) {
        /**
         * http://localhost:8080/request-param-default?username=
         * 위와 같이 username에 값이 없는 상태로 보냈다.
         *
         * 결과 : username = guest, age = -1
         *
         * 즉, 값이 없는 상태(빈 문자열)까지 defaultValue가 체크해서 값을 넣어준다!
         */

        log.info("username = {}, age = {}", username, age);
        return "ok";
    }

    /**
     * 파라미터를 Map, MultiValueMap으로 조회할 수 있다.
     * @RequestParam Map , Map(key=value)
     *
     * @RequestParam MultiValueMap
     *      MultiValueMap(key=[value1, value2, ...] ex) (key=userIds, value=[id1, id2])
     * 파라미터의 값이 1개가 확실하다면 Map 을 사용해도 되지만, 그렇지 않다면 MultiValueMap 을 사용하자
     */
    @ResponseBody
    @RequestMapping("/request-param-map")
    public String requestParamMap(@RequestParam Map<String, Object> paramMap) {
        log.info("username = {}, age = {}", paramMap.get("username"), paramMap.get("age"));
        return "ok";
    }

//    @ResponseBody
//    @RequestMapping("/model-attribute-v1-prev")
//    public String modelAttributeV1_Prev(@RequestParam String username, @RequestParam int age) {
//        HelloData helloData = new HelloData();
//        helloData.setUsername(username);
//        helloData.setAge(age);
//
//        log.info("username = {}, age = {}", helloData.getUsername(), helloData.getAge());
//        log.info("helloData = {}", helloData);
//
//        return "ok";
//    }

    /**
     * 보통은 위와 같은 과정을 통해 객체에 세팅을 한다.
     *
     * 이러한 과정을 한번에 해주는 것이 바로 @ModelAttribute이다.
     */
    @ResponseBody
    @RequestMapping("/model-attribute-v1")
    public String modelAttributeV1(@ModelAttribute HelloData helloData) {
        log.info("helloData = {}", helloData);
        return "ok";
    }
    /**
     * 어떻게 이런 과정이 되는 걸까? 마치 마법같다.
     *
     * 스프링 MVC는 @ModelAttribute 가 있으면 다음을 실행한다.
     * 1. HelloData 객체를 생성한다.
     * 2. 요청 파라미터의 이름으로 HelloData 객체의 프로퍼티를 찾는다.
     * 3. 그리고 해당 프로퍼티의 setter를 호출해서 파라미터의 값을 입력(바인딩) 한다.
     * 예) 파라미터 이름이 username 이면 setUsername() 메서드를 찾아서 호출하면서 값을 입력한다.
     */

    @ResponseBody
    @RequestMapping("/model-attribute-v2")
    public String modelAttributeV2(HelloData helloData) { // ModelAttribute 생략해도 됩니다.
        log.info("helloData = {}", helloData);
        return "ok";
    }
    /**
     * @ModelAttribute 는 생략할 수 있다.
     * 그런데 @RequestParam 도 생략할 수 있으니 혼란이 발생할 수 있다.
     * 스프링은 해당 생략시 다음과 같은 규칙을 적용한다.
     * String , int , Integer 같은 단순 타입 = @RequestParam 이 생략이 되었구나~
     * 나머지 = @ModelAttribute (argument resolver 로 지정해둔 타입 외)
     */
}
