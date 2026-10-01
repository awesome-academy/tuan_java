package com.tuanhv.tripgoapi.controller.admin;

import com.tuanhv.tripgoapi.service.admin.AdminDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminDashboardController {

    private final AdminDashboardService dashboardService;

    @GetMapping
    public String dashboard(Model model) {
        model.addAttribute(
                "dashboard",
                dashboardService.getDashboard()
        );

        return "admin/dashboard";
    }
}
