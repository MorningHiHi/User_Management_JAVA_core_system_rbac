package com.example.core_system_rbac.controllers;

import com.example.core_system_rbac.services.LoggedUserManagementService;
import com.example.core_system_rbac.services.LoginCountService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {

    private final LoggedUserManagementService loggedUserManagementService;
    private final LoginCountService loginCountService;

    public MainController(
            LoggedUserManagementService loggedUserManagementService,
            LoginCountService loginCountService) {
        this.loggedUserManagementService = loggedUserManagementService;
        this.loginCountService = loginCountService;
    }

    @GetMapping("/main")
    public String home(@RequestParam(required = false) String logout, Model model) {

        if (logout != null) {
            loggedUserManagementService.setUsername(null);
        }
        String username = loggedUserManagementService.getUsername();
        int times = loginCountService.getLoginCount();
        if (username == null) {
            return "redirect:/";
        }
        model.addAttribute("username", username);
        model.addAttribute("loginCount",  times);
        return "main.html";
    }


}
