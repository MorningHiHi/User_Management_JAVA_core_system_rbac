package com.example.core_system_rbac.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class StaticPagesController {
    @RequestMapping("/home")
    public String homepage() {
        return "forward:homepage.html";
    }
    @RequestMapping("/homedynamic")
    public String homedynamic(Model page) {
        page.addAttribute("username", "Katy");
        page.addAttribute("color", "red");
        return "homedynamic.html";
    }
}
