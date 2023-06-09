package com.avg.kreditantrag.process;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@SuppressWarnings("SpringMVCViewInspection") // it is there
public class IndexController {
    @GetMapping("/")
    public String index() {
        return "index.html";
    }
}
