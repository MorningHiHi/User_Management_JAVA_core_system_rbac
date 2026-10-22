package com.example.core_system_rbac.controllers;

import com.example.core_system_rbac.dtos.response.AuthResponse;
import com.example.core_system_rbac.services.LoginProcessor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    LoginProcessor loginProcessor = new LoginProcessor();

    @GetMapping("/login")
    public String loginGet() {
        return "login.html";
    }

    @PostMapping("/login")
    public String loginPost(@RequestParam String username, @RequestParam String password, Model model) {
        loginProcessor.setUsername(username);
        loginProcessor.setPassword(password);
        boolean vali= loginProcessor.login();
        if (vali) {
            model.addAttribute("message", "ok sang oi");
        } else {
            model.addAttribute("message", "chua duoc roi!!");
        }
        return "login.html";
    }
}
