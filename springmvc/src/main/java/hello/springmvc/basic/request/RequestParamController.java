package hello.springmvc.basic.request;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
    public String requestParamMap(@RequestParam Map<String, Object> paramMap){
        log.info("username = {}, age = {}", paramMap.get("username"), paramMap.get("age"));
        return "ok";
    }


}
