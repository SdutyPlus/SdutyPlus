package com.d205.sdutyplus.global.policy;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @GetMapping(value="/")
    public String showPolicy() {
        return "policy.html";
    }
}
