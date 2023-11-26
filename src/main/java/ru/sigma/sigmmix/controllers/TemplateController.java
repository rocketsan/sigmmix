package ru.sigma.sigmmix.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import ru.sigma.sigmmix.model.Template;
import ru.sigma.sigmmix.repositories.TemplateRepository;

import java.util.Comparator;
import java.util.List;

@Controller
@Deprecated
public class TemplateController {

    @Value("${application.name}")
    private String applicationName;

    @ModelAttribute
    public void addCommonAttributes(Model model) {
        model.addAttribute("appName", applicationName);
    }

    @Autowired
    private TemplateRepository templateRepository;

    @GetMapping("/templates")
    public String listHosts(Model model) {
        List<Template> templates = templateRepository.findAll();
        templates.sort(Comparator.comparing(Template::getId));
        model.addAttribute("templates", templates);
        model.addAttribute("pageTitle", "Шаблоны");
        return "templates";
    }


    @GetMapping("/add-template")
    public String templateAdd(Model model) {
        model.addAttribute("template", new Template());
        model.addAttribute("pageTitle", "Шаблоны");
        return "edit-template";
    }

}
