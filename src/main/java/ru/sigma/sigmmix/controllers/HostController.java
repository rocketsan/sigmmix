package ru.sigma.sigmmix.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import ru.sigma.sigmmix.model.Host;
import ru.sigma.sigmmix.repositories.HostRepository;

import java.util.Comparator;
import java.util.List;

@Controller
public class HostController {

    @Value("${application.name}")
    private String applicationName;

    @ModelAttribute
    public void addCommonAttributes(Model model) {
        model.addAttribute("appName", applicationName);
    }

    @Autowired
    private HostRepository hostRepository;

    @GetMapping("/hosts")
    public String listHosts(Model model) {
        List<Host> hosts = hostRepository.findAll();
        hosts.sort(Comparator.comparing(Host::getId));
        model.addAttribute("hosts", hosts);
        model.addAttribute("pageTitle", "Хосты");
        return "hosts";
    }
}
