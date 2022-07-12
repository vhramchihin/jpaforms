package com.example.jpaforms.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class RouteController {
	@RequestMapping("/forms/**")
    public String redirect() {
        return "forward:/";
    }
}