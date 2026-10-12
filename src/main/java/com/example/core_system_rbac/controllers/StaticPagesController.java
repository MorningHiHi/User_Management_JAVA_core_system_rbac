package com.example.core_system_rbac.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class StaticPagesController {
    @RequestMapping("/home")
    public String homepage() {
        return "forward:homepage.html";
    }

    @RequestMapping("/homedynamic/{color}")
    public String homedynamic(@PathVariable String color, @RequestParam String key, Model page) {
        page.addAttribute("username", key);
        page.addAttribute("color", color);
        return "homedynamic.html";
    }
}
