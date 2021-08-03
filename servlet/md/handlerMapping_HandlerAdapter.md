# HandlerMApping & HandlerAdapter

## HandlerMapping

우선순위는 다음과 같다.

- 0 = RequestMappingHandlerMapping : 애노테이션 기반의 컨트롤러인 @RequestMapping에서 사용
- 1 = BeanNameUrlHandlerMapping : 스프링 빈의 이름으로 핸들러를 찾는다.

그래서 OldController에서 Bean 이름으로 요청을 받게 되는 것이다.

```java

@Component("/springmvc/old-controller") // 스프링 빈의 이름 : 빈의 이름으로 URL을 매핑할 것이다.
public class OldController implements Controller { // 컨트롤러 인터페이스 != 컨트롤러 어노테이션

    @Override
    public ModelAndView handleRequest(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        System.out.println("OldController.handleRequest");
        return null;
    }
}
```

## HandlerAdapter

- 0 = RequestMappingHandlerAdapter : 애노테이션 기반의 컨트롤러인 @RequestMapping에서 사용
- 1 = HttpRequestHandlerAdapter : HttpRequestHandler 처리
- 2 = SimpleControllerHandlerAdapter : Controller 인터페이스(애노테이션X, 과거에 사용했던 컨트롤러 인터페이스) 처리

그래서 oldController에서 핸들러 어댑터가 요청이 된 것도 3번 째 우선순위인 SimpleControllerHandlerAdapter에서 Controller Interface를 상속하고 있기 때문에 호출이 되는 것이다. 그 코드는 다음과 같다.

```java
public class SimpleControllerHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean supports(Object handler) {
        return (handler instanceof Controller);
    }

    @Override
    @Nullable
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return ((Controller) handler).handleRequest(request, response);
    }

    @Override
    public long getLastModified(HttpServletRequest request, Object handler) {
        if (handler instanceof LastModified) {
            return ((LastModified) handler).getLastModified(request);
        }
        return -1L;
    }

}
```

핸들러 매핑도, 핸들러 어댑터도 모두 순서대로 찾고 만약 없으면 다음 순서로 넘어간다.
> 1. 핸들러 매핑으로 핸들러 조회

1. HandlerMapping 을 순서대로 실행해서, 핸들러를 찾는다.
2. 이 경우 빈 이름으로 핸들러를 찾아야 하기 때문에 이름 그대로 빈 이름으로 핸들러를 찾아주는 BeanNameUrlHandlerMapping 가 실행에 성공하고 핸들러인 OldController 를 반환한다.

> 2. 핸들러 어댑터 조회

1. HandlerAdapter 의 supports() 를 순서대로 호출한다.
2. SimpleControllerHandlerAdapter 가 Controller 인터페이스를 지원하므로 대상이 된다.

> 3. 핸들러 어댑터 실행

1. 디스패처 서블릿이 조회한 SimpleControllerHandlerAdapter 를 실행하면서 핸들러 정보도 함께 넘겨준다.
2. SimpleControllerHandlerAdapter 는 핸들러인 OldController 를 내부에서 실행하고, 그 결과를 반환한다.

> 정리 - OldController 핸들러매핑, 어댑터

- OldController 를 실행하면서 사용된 객체는 다음과 같다.
- HandlerMapping = BeanNameUrlHandlerMapping
- HandlerAdapter = SimpleControllerHandlerAdapter

## HttpRequestHandler

- 핸들러 매핑과, 어댑터를 더 잘 이해하기 위해 Controller 인터페이스가 아닌 다른 핸들러를 알아보자.
- HttpRequestHandler 핸들러(컨트롤러)는 서블릿과 가장 유사한 형태의 핸들러이다.

```java
@Component("/springmvc/request-handler")
public class MyHttpRequestHandler implements HttpRequestHandler {
    @Override
    public void handleRequest(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        System.out.println("MyHttpRequestHandler.handleRequest");
    }
}
```

1. 어노테이션 없다.
2. 위에 설명한 우선순위의 두번째에 걸리게 된다. HttpRequestHandler를 가보면 다음과 같다. 핸들러 어댑터를 호출하게 되는데

````java
public class HttpRequestHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean supports(Object handler) {
        return (handler instanceof HttpRequestHandler);
    }

    @Override
    @Nullable
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        ((HttpRequestHandler) handler).handleRequest(request, response);
        return null;
    }

    @Override
    public long getLastModified(HttpServletRequest request, Object handler) {
        if (handler instanceof LastModified) {
            return ((LastModified) handler).getLastModified(request);
        }
        return -1L;
    }

}
````

위 핸들러 어댑터에서 핸들이 호출이 되는데, 이 호출이 되는 위치는 DispatcherServlet이다. 그러면 핸들 메서드 안에서 handleRequest를 호출하는데, 이는 우리가 만든 다음 함수가 호출이 되어서 최종적으로 MyHttpRequestHandler.handleRequest가 출력되는 것이다.

```java
@Component("/springmvc/request-handler")
public class MyHttpRequestHandler implements HttpRequestHandler {
    @Override
    public void handleRequest(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        System.out.println("MyHttpRequestHandler.handleRequest");
    }
}
```

순서를 정리해보자

> 1. 핸들러 매핑으로 핸들러 조회

1. HandlerMapping 을 순서대로 실행해서, 핸들러를 찾는다.
2. 이 경우 빈 이름으로 핸들러를 찾아야 하기 때문에 이름 그대로 빈 이름으로 핸들러를 찾아주는 BeanNameUrlHandlerMapping 가 실행에 성공하고 핸들러인 MyHttpRequestHandler 를 반환한다.

> 2. 핸들러 어댑터 조회

1. HandlerAdapter 의 supports() 를 순서대로 호출한다.
2. HttpRequestHandlerAdapter 가 HttpRequestHandler 인터페이스를 지원하므로 대상이 된다.

> 3. 핸들러 어댑터 실행

1. 디스패처 서블릿이 조회한 HttpRequestHandlerAdapter 를 실행하면서 핸들러 정보도 함께 넘겨준다.
2. HttpRequestHandlerAdapter 는 핸들러인 MyHttpRequestHandler 를 내부에서 실행하고, 그 결과를 반환한다.

> 정리 - MyHttpRequestHandler 핸들러매핑, 어댑터

- MyHttpRequestHandler 를 실행하면서 사용된 객체는 다음과 같다.
- HandlerMapping = BeanNameUrlHandlerMapping
- HandlerAdapter = HttpRequestHandlerAdapter

> @RequestMapping

- 조금 뒤에서 설명하겠지만, 가장 우선순위가 높은 핸들러 매핑과 핸들러 어댑터는 RequestMappingHandlerMapping, RequestMappingHandlerAdapter 이다.
- @RequestMapping 의 앞글자를 따서 만든 이름인데, 이것이 바로 지금 스프링에서 주로 사용하는 애노테이션 기반의 컨트롤러를 지원하는 매핑과 어댑터이다.
- 실무에서는 99.9% 이 방식의 컨트롤러를 사용한다.
