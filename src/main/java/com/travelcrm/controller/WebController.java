package com.travelcrm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

@Controller
public class WebController {

    @GetMapping("/")
    public String dashboard(Model model) {
        // You can add data to the model here if needed for the dashboard
        return "dashboard";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/vendors")
    public String vendors() {
        return "vendors";
    }

    @GetMapping("/itineraries")
    public String itineraries() {
        return "itineraries";
    }

    @GetMapping("/travels")
    public String travels() {
        return "travels";
    }

    @GetMapping("/reservations")
    public String reservations() {
        return "reservations";
    }

    @GetMapping("/customers")
    public String customers() {
        return "customers";
    }
} 