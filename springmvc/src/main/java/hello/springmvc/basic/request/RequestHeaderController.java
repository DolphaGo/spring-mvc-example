package hello.springmvc.basic.request;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpMethod;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class RequestHeaderController {

    @RequestMapping("/headers")
    public String headers(
            HttpServletRequest request,
            HttpServletResponse response,
            HttpMethod httpMethod,
            Locale locale,
            @RequestHeader MultiValueMap<String, String> headerMap,
            @RequestHeader("host") String host,
            @CookieValue(value = "myCookie", required = false, defaultValue = "aaa") String cookie
    ) {

        log.info("request={}", request);
        log.info("response={}", response);
        log.info("httpMethod={}", httpMethod);
        log.info("locale={}", locale); // 가장 우선순위가 높은 것으로 지정됨. (더 자세히 알고 싶다면, @LocaleResolver 확인)
        log.info("headerMap={}", headerMap);
        log.info("header host={}", host);
        log.info("myCookie={}", cookie);

        return "ok";
    }

    /**
     * MultiValueMap
     * Map과 유사한데, 하나의 키에 여러 값을 받을 수 있다.
     * HTTP header, HTTP 쿼리 파라미터와 같이 하나의 키에 여러 값을 받을 때 사용한다.
     * keyA=value1&keyA=value2
     *   MultiValueMap<String, String> map = new LinkedMultiValueMap();
     *   map.add("keyA", "value1");
     *   map.add("keyA", "value2");
     *
     *   //[value1,value2]
     *   List<String> values = map.get("keyA");
     */
}
