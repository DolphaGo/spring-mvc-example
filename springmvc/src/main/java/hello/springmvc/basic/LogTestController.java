package hello.springmvc.basic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class LogTestController {
//    private final Logger log = LoggerFactory.getLogger(getClass()); // 또는 Xxx.class 또는 Lombok의 @Slf4j

    @RequestMapping("/log-test")
    public String logTest() {
        String name = "Spring";

        System.out.println("name = " + name); // 얘는 레벨도 없고, 무조건 출력이 된다.

        /**
         * 운영 서버(info), 개발 서버(debug)에 따라서 로그 레벨을 달리 설정할 때가 필요한데, System.out.println 같은 경우엔 모든 레벨을 다 볼 수 있다는 것.
         * 이게 정말 단점임. 로그를 써야하는 이유!
         */

        /**
         * 위에서 아래로 내려갈 수록 로거 레벨이 높아진다.
         * application.yml(properties)에서 logging.level.hello.springmvc = debug 처럼 로그 레벨을 설정한다.
         */
        log.trace(" trace my log =" + name); // 이렇게 표현하지 마라. Why ?  자바에서는 연산을 해버린다.
        // 1. name -> Spring
        // 2. "trace my log ="+ "Spring"
        // 3. "trace my log =Spring 준비해놓고나서,
        // 4. 어, trace네? 현재 로그 레벨이 info인데, 출력안해!
        // => 결론 : 이미 문자열 연산은 다 해버렸는데, 출력하지 않는, 즉 사용하지 않는 연산 => 성능 악화
        // 따라서 + 가 아닌, 파라미터로 넘겨야 한다. 그래야 성능 최적화에 일조를 한다. 파라미터를 넣기 전에 로그 레벨을 보고, 파라미터를 넣을지 말지 결정하기 때문이다.

        log.trace("trace log = {}", name);
        log.debug("debug log= {}", name);
        log.info("info log = {}", name);
        log.warn("warn log = {}", name);
        log.error("error log = {}", name);

        return "ok";
    }
}

/**
 * 로그 사용시 장점
 *
 * 쓰레드 정보, 클래스 이름 같은 부가 정보를 함께 볼 수 있고, 출력 모양을 조정할 수 있다.
 * 로그 레벨에 따라 개발 서버에서는 모든 로그를 출력하고, 운영서버에서는 출력하지 않는 등 로그를 상황에 맞게 조절할 수 있다.
 * 시스템 아웃 콘솔에만 출력하는 것이 아니라, 파일이나 네트워크 등, 로그를 별도의 위치에 남길 수 있다.
 * 특히 파일로 남길 때는 일별, 특정 용량에 따라 로그를 분할하는 것도 가능하다.
 * 성능도 일반 System.out보다 좋다. (내부 버퍼링, 멀티 쓰레드 등등) 그래서 실무에서는 꼭 로그를 사용해야 한다.
 */
