package com.travelcrm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard";
    }

    @GetMapping("/travels")
    public String travels() {
        return "travels";
    }

    @GetMapping("/vendors")
    public String vendors() {
        return "vendors";
    }
} 