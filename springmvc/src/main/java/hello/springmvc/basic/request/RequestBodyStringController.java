package hello.springmvc.basic.request;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class RequestBodyStringController {

    @PostMapping("/request-body-string-v1")
    public void requestBodyStringV1(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final ServletInputStream inputStream = request.getInputStream();
        final String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);

        log.info("messageBody={}", messageBody);
        response.getWriter().write("ok");
    }

    /**
     * 위의 코드를 다음과 같이 줄일 수도 있다.
     *
     * > 참고
     * > @Controller 의 사용 가능한 파라미터 목록은 다음 공식 메뉴얼에서 확인할 수 있다.
     * > https://docs.spring.io/spring-framework/docs/current/reference/html/web.html#mvc-ann-arguments
     *
     * > @Controller 의 사용 가능한 응답 값 목록은 다음 공식 메뉴얼에서 확인할 수 있다.
     * > https://docs.spring.io/spring-framework/docs/current/reference/html/web.html#mvc-ann-
     */
    @PostMapping("/request-body-string-v2")
    public void requestBodyStringV2(InputStream inputStream, Writer writer) throws IOException {
        final String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
        log.info("messageBody={}", messageBody);
        writer.write("ok");
    }

    /**
     * 메세지 컨버터
     */
    @PostMapping("/request-body-string-v3")
    public HttpEntity<String> requestBodyStringV3(HttpEntity<String> httpEntity) {
        log.info("messageBody={}", httpEntity.getBody());
        return new HttpEntity<>("ok");
    }

    /**
     * HttpEntity에 대하여.......
     *
     * 스프링 MVC는 다음 파라미터를 지원한다.
     *
     * HttpEntity: HTTP header, body 정보를 편리하게 조회
     * 메시지 바디 정보를 직접 조회
     * 요청 파라미터를 조회하는 기능과 관계 없음 @RequestParam X, @ModelAttribute X
     * HttpEntity는 응답에도 사용 가능
     * 메시지 바디 정보 직접 반환
     * 헤더 정보 포함 가능
     * view 조회X
     * HttpEntity 를 상속받은 다음 객체들도 같은 기능을 제공한다.
     *
     * RequestEntity
     * HttpMethod, url 정보가 추가, 요청에서 사용
     * ResponseEntity
     * HTTP 상태 코드 설정 가능, 응답에서 사용
     * > 참고
     * 스프링MVC 내부에서 HTTP 메시지 바디를 읽어서 문자나 객체로 변환해서 전달해주는데, 이때 HTTP 메시지 컨버터( HttpMessageConverter )라는 기능을 사용한다.
     */

    @PostMapping("/request-body-string-v3-1")
    public HttpEntity<String> requestBodyStringV3_1(HttpEntity<String> httpEntity) {
        log.info("messageBody={}", httpEntity.getBody());
        return new ResponseEntity<>("ok", HttpStatus.CREATED); // ResponseEntity도 HttpEntity를 상속받아서 다음과 같이 리턴할 수 있음(응답에 대한 다양한 기능 추가)
    }

    /**
     * V3을 한 단계 더 업그레이드 한 버전
     */
    @ResponseBody
    @PostMapping("/request-body-string-v4")
    public String requestBodyStringV4(@RequestBody String messageBody) {
        log.info("messageBody={}", messageBody);
        return "ok";
    }
    /**
     * @RequestBody 를 사용하면 HTTP 메시지 바디 정보를 편리하게 조회할 수 있다.
     * 참고로 헤더 정보가 필요하다면 HttpEntity 를 사용하거나 @RequestHeader 를 사용하면 된다.
     * 이렇게 메시지 바디를 직접 조회하는 기능은 요청 파라미터를 조회하는 @RequestParam , @ModelAttribute 와는 전혀 관계가 없다.
     *
     * 요청 파라미터 vs HTTP 메시지 바디
     * 요청 파라미터를 조회하는 기능: @RequestParam , @ModelAttribute
     * HTTP 메시지 바디를 직접 조회하는 기능: @RequestBody
     *
     * @ResponseBody 를 사용하면 응답 결과를 HTTP 메시지 바디에 직접 담아서 전달할 수 있다.
     * 물론 이 경우에도 view를 사용하지 않는다.
     */
}
