package ru.sigma.sigmmix.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class PagesController {

    @Value("${application.name}")
    private String applicationName;

    @ModelAttribute
    public void addCommonAttributes(Model model) {
        model.addAttribute("appName", applicationName);
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("pageTitle", "Панель управления");
        return "index";
    }

    @GetMapping("/hosts")
    public String hosts(Model model) {
        model.addAttribute("pageTitle", "Хосты");
        return "hosts";
    }

    @GetMapping("/templates")
    public String templates(Model model) {
        model.addAttribute("pageTitle", "Шаблоны");
        return "templates";
    }

    @GetMapping("/notification")
    public String notification(Model model) {
        model.addAttribute("pageTitle", "Уведомления");
        return "notification";
    }

}
