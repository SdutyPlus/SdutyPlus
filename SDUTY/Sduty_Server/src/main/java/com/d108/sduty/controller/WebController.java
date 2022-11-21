package com.d108.sduty.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WebController {
	@RequestMapping("/privacy")
	public String index() {
		System.out.println("asldkjflkasdjflkajsdf");
		return "index";
	}
}
