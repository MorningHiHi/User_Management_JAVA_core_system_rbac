package com.example.core_system_rbac.controllers;

import com.example.core_system_rbac.services.LoggedUserManagementService;
import com.example.core_system_rbac.services.LoginProcessor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {
    private final LoginProcessor loginProcessor;

    // Spring IoC sẽ tự động inject LoginProcessor vào Controller
    public LoginController(LoginProcessor loginProcessor) {
        this.loginProcessor = loginProcessor;
    }
    @GetMapping("/")
    public String loginGet() {
        return "login.html";
    }

    @PostMapping("/login")
    public String loginPost(@RequestParam String username, @RequestParam String password, Model model) {
        loginProcessor.setUsername(username);
        loginProcessor.setPassword(password);
        boolean vali= loginProcessor.login();

        if (vali) {
            return "redirect:/main";
        }

        model.addAttribute("message", "Login failed!");
        return "login.html";
    }

}
