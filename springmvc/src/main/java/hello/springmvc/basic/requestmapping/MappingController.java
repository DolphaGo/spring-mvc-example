package hello.springmvc.basic.requestmapping;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MappingController {
    private Logger log = LoggerFactory.getLogger(getClass());

    /**
     * 기본 요청
     * 둘다 허용 /hello-basic, /hello-basic/
     * HTTP 메서드 모두 허용 GET, HEAD, POST, PUT, PATCH, DELETE
     */
    @RequestMapping({ "/hello-basic", "/hello-go" }) // 배열로 여러개 가능
    public String helloBasic() {
        log.info("hello-basic");
        return "ok";
    }

    @RequestMapping(value = { "/hello-basic_1", "/hello-go_1" }, method = RequestMethod.GET) // GET 요청만 매핑되도록
    public String helloBasic_1() {
        log.info("hello-basic_1");
        return "ok";
    }

    @GetMapping("/mapping-get-v2")
    public String mappingGetV2() {
        log.info("mapping-get-v2");
        return "ok";
    }

    /**
     * PathVariable 사용
     * 변수명이 같으면 생략 가능
     * @PathVariable("userId") String userId -> @PathVariable userId
     * /mapping/userA
     */
    @GetMapping("/mapping/{userId}")
    public String mappingPath(@PathVariable("userId") String data) { // PathVariable과 변수명이 같으면 생략할 수 있다. 즉, @PathVariable String userId 이렇게도 가능하다. (data가 아니라)
        log.info("mappingPath userId = {}", data);
        return "ok";
    }

    /**
     * PathVariable 사용 다중
     */
    @GetMapping("/mapping/users/{userId}/orders/{orderId}")
    public String mappingPath(@PathVariable String userId, @PathVariable Long orderId) {
        log.info("mappingPath userId = {}, orderId={}", userId, orderId);
        return "ok";
    }

    /**
     * 특정 파라미터로 매핑
     * 파라미터로 추가 매핑
     * params="mode",
     * params="!mode"
     * params="mode=debug"
     * params="mode!=debug" (! = )
     * params = {"mode=debug","data=good"}
     */
    @GetMapping(value = "/mapping-param", params = "mode=debug") // 특정 파라미터가 있어야, 요청 응답을 받는다는 의미(거의 잘 사용하지는 않음)
    public String mappingParam() {
        log.info("mappingParam");
        return "ok";
    }

    /**
     * 특정 헤더로 추가 매핑
     * headers="mode",
     * headers="!mode"
     * headers="mode=debug"
     * headers="mode!=debug" (! = )
     */
    @GetMapping(value = "/mapping-header", headers = "mode=debug") // 그냥 localhost:8080/mapping-header로는 호출되지 않음. 헤더에 값을 넣어줘야 함
    public String mappingHeader() {
        log.info("mappingHeader");
        return "ok";
    }

    /**
     * 미디어 타입 조건 매핑 - HTTP 요청 ContentType, consume
     *
     * Content-Type 헤더 기반 추가 매핑 Media Type * consumes="application/json"
     * consumes="!application/json"
     * consumes="application/*"
     * consumes="*\/*"
     * MediaType.APPLICATION_JSON_VALUE
     *
     * HTTP 요청의 Content-Type 헤더를 기반으로 미디어 타입으로 매핑한다.
     * 만약 맞지 않으면 HTTP 415 상태코드(Unsupported Media Type)을 반환한다.
     */
    @PostMapping(value = "/mapping-consume", consumes = "application/json") // 반드시 요청을 보낼 때 Content-Type이 application/json이어야 한다! (나는 application/json으로 명시한 요청만 읽을거야)
    public String mappingConsumes() {
        log.info("mappingConsumes");
        return "ok";
    }


    /** Accept 헤더 기반으로 조건 매핑하기
     *
     * Accept 헤더 기반 Media Type * produces = "text/html"
     * produces = "!text/html" * produces = "text/*"
     * produces = "*\/*"
     *
     * 예상하던 Accept 헤더가 아니라면,
     * {
     *     "timestamp": "2021-08-09T16:55:11.877+00:00",
     *     "status": 406,
     *     "error": "Not Acceptable",
     *     "path": "/mapping-produce"
     * }
     *
     * 위와 같이, 406, Not Acceptable이 뜬다.
     */
    @PostMapping(value = "/mapping-produce", produces = MediaType.TEXT_HTML_VALUE) // 반드시 text/html로 보내줘! 라고 헤더에 담아서 요청할 때만 이 핸들러 매핑이 동작한다(produces)
    public String mappingProduces() {
        log.info("mappingProduces");
        return "ok";
    }
    /**
     * consumes는 클라이언트가 서버에게 보내는 데이터 타입을 명시한다. - Content-Type
     * produces는 서버가 클라이언트에게 반환하는 데이터 타입을 명시한다. - Accept
     */

}
