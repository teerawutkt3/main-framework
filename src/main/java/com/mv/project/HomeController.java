package com.mv.project;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
    @RequestMapping(value = "/**/{[path:[^\\.]*}")
    public String redirectUi() {
        return "forward:index.html";
    }

}
