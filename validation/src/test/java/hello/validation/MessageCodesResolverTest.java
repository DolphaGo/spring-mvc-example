package hello.validation;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DefaultMessageCodesResolver;
import org.springframework.validation.MessageCodesResolver;

public class MessageCodesResolverTest {
    MessageCodesResolver codesResolver = new DefaultMessageCodesResolver();

    /**
     * DefaultMessageCodesResolve의 기본 메시지 생성 규칙
     *
     * > 객체 오류
     * 1. code + "." + object name
     * 2. code
     *
     * 예) 오류 코드 : required, object name : item
     * 1. required.item
     * 2. required
     *
     * > 필드 오류
     * 1. code + "." + object name + "." + field
     * 2. code + "." + field
     * 3. code + "." + field type
     * 4. code
     *
     * 동작 방식
     * - rejectValue(), reject()는 내부에서 MessageCodesResolver를 사용한다. 여기에서 메시지 코드들을 생성한다.
     * - FieldError, ObjectError의 생성자를 보면, 오류 코드를 하나가 아니라 여러 오류코드를 가질 수 있다.
     * - MessageCodesResolver를 통해서 생성된 순서대로 오류코드를 보관한다.
     * - 이 부분을 BindingResult 로그를 통해서 확인해보자.
     */

    @Test
    void messageCodesResolverObject() {
        final String[] messageCodes = codesResolver.resolveMessageCodes("required", "item");
        Arrays.stream(messageCodes).forEach(messageCode -> System.out.println("messageCode = " + messageCode));
        //messageCode = required.item
        //messageCode = required
        assertThat(messageCodes).containsExactly("required.item", "required");
    }

    @Test
    void messageCodesResolverField() {
        final String[] messageCodes = codesResolver.resolveMessageCodes("required", "item", "itemName", String.class);
        Arrays.stream(messageCodes).forEach(messageCode -> System.out.println("messageCode = " + messageCode));
        //messageCode = required.item.itemName 에러코드 + 객체명 + 필드명
        //messageCode = required.itemName 에러코드 + 필드명
        //messageCode = required.java.lang.String
        //messageCode = required 에러코드

        /**
             BindingResult.rejectValue("itemName", "required") // 내부적으로 resolveMessageCodes를 사용한다.
           => new FieldError("item", "itemName", null, false, messageCodes, ...); // 위와 같이 필드를 만들어주기 때문에 그 중 하나(가장 세밀한 것)를 찾아오는 것
         */

        assertThat(messageCodes).containsExactly(
                "required.item.itemName",
                "required.itemName",
                "required.java.lang.String",
                "required"
        );
    }
}
