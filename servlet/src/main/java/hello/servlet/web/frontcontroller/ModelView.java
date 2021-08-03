package hello.servlet.web.frontcontroller;

import java.util.HashMap;
import java.util.Map;

public class ModelView { // SpringMVC에는 비슷한 기능의 ModelAndView 라는 것이 있다.
    private String viewName;
    private Map<String, Object> model = new HashMap<>();

    public ModelView(final String viewName) {
        this.viewName = viewName;
    }

    public String getViewName() {
        return viewName;
    }

    public Map<String, Object> getModel() {
        return model;
    }

    public void setViewName(final String viewName) {
        this.viewName = viewName;
    }

    public void setModel(final Map<String, Object> model) {
        this.model = model;
    }
}


