package ru.sigma.sigmmix.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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

    @GetMapping("/add-host")
    public String hostAdd(Model model) {
        model.addAttribute("host", new Host());
        model.addAttribute("pageTitle", "Хосты");
        return "edit-host";
    }

    @PostMapping("/save-host")
    public String addHost(@ModelAttribute Host host) {
        System.out.println("POST /save-host/ "+host);
        hostRepository.save(host);
        return "redirect:/hosts";
    }

    @GetMapping("/edit-host/{id}")
    public String editHostForm(@PathVariable Long id, Model model) {
        Host host = hostRepository.findById(id).orElse(new Host());
        model.addAttribute("host", host);
        model.addAttribute("pageTitle", "Хосты");
        return "edit-host";
    }

    /*
    @PostMapping("/edit-host/{id}")
    public String editHost(@PathVariable Long id, @ModelAttribute Host editedHost) {
        System.out.println("POST /edit-host/"+id);
        Host existingHost = hostRepository.findById(id).orElse(null);

        if (existingHost == null) {
            // Обработка случая, если хост не найден
            return "redirect:/hosts";
        }

        // Обновите поля существующего пользователя данными из editedUser
        existingHost.setHostname(editedHost.getHostname());
        existingHost.setIpAddress(editedHost.getIpAddress());
        existingHost.setInterfaceType(editedHost.getInterfaceType());
        System.out.println("isActive="+editedHost.isActive());
        existingHost.setActive(editedHost.isActive());

        hostRepository.save(existingHost);
        return "redirect:/hosts";
    }
    */
}
