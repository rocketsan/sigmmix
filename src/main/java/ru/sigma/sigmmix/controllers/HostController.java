package ru.sigma.sigmmix.controllers;

import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.sigma.sigmmix.model.Host;
import ru.sigma.sigmmix.repositories.HostRepository;
import ru.sigma.sigmmix.services.monitoring.MonitoringServiceBase;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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

    @GetMapping("/getServices")
    @ResponseBody
    public List<String> getMonitoringServices() {
        Reflections reflections = new Reflections();

        return reflections.getSubTypesOf(MonitoringServiceBase.class)
                .stream()
                //.map(Class::getSimpleName)
                .map(Class::getName)
                .collect(Collectors.toList());
    }

    @GetMapping("/add-host")
    public String hostAdd(Model model) {
        model.addAttribute("host", new Host());
        model.addAttribute("pageTitle", "Хосты");
        model.addAttribute("classNames", getMonitoringServices());
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
        model.addAttribute("classNames", getMonitoringServices());
        return "edit-host";
    }

    @GetMapping("/delete-host/{id}")
    public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Host host = hostRepository.findById(id).orElse(null);

        if (host == null) {
            // Обработка случая, если хост не найден
            redirectAttributes.addFlashAttribute("errorMessage", "Хост не найден!");
            return "redirect:/hosts"; // или другой URL
        }
        if (host.isActive()) {
            // Хост активный! Сначала выключите!
            redirectAttributes.addFlashAttribute("errorMessage", "Нельзя удалить активный хост!");
            return "redirect:/hosts";
        }

        host.setRemoved(true); // removed flag
        hostRepository.save(host);
        redirectAttributes.addFlashAttribute("successMessage", "Хост успешно удален");

        return "redirect:/hosts";
    }


    @GetMapping("/edit-host/{id}/{action}")
    public String editHost(@PathVariable Long id, @PathVariable String action, RedirectAttributes redirectAttributes) {
        System.out.println("POST /edit-host/"+id+"/"+action);

        Host existingHost = hostRepository.findById(id).orElse(null);

        if (existingHost == null) {
            // Обработка случая, если хост не найден
            redirectAttributes.addFlashAttribute("errorMessage", "Хост не найден!");
            return "redirect:/hosts";
        }

        if (action.equals("enable")) {
            System.out.println("isActive=>true");
            existingHost.setActive(true);
            hostRepository.save(existingHost);
            redirectAttributes.addFlashAttribute("successMessage", "Мониторинг хоста включен");
        } else if (action.equals("disable")) {
            System.out.println("isActive=>false");
            existingHost.setActive(false);
            hostRepository.save(existingHost);
            redirectAttributes.addFlashAttribute("successMessage", "Мониторинг хоста выключен");
        }

        return "redirect:/hosts";
    }
}
