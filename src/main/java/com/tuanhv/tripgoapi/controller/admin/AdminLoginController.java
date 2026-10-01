package com.tuanhv.tripgoapi.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminLoginController {

    @GetMapping("/admin/login")
    public String login() {
        return "admin/login";
    }

    @GetMapping("/admin/access-denied")
    public String accessDenied() {
        return "admin/access-denied";
    }
}
