package com.example.core_system_rbac.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class StaticPagesController {
    @RequestMapping("/home")
    public String homepage() {
        return "homepage.html";
    }
}
