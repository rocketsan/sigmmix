package ru.sigma.sigmmix.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.sigma.sigmmix.model.Host;
import ru.sigma.sigmmix.model.RawData;
import ru.sigma.sigmmix.model.Subscription;
import ru.sigma.sigmmix.model.User;
import ru.sigma.sigmmix.repositories.HostRepository;
import ru.sigma.sigmmix.repositories.SubscriptionRepository;
import ru.sigma.sigmmix.repositories.UserRepository;

import java.util.Comparator;
import java.util.List;

@Controller
public class SubscriptionController {

    @Value("${application.name}")
    private String applicationName;

    @ModelAttribute
    public void addCommonAttributes(Model model) {
        model.addAttribute("appName", applicationName);
    }

    @Autowired
    private SubscriptionRepository subscriptionRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private HostRepository hostRepository;

    @GetMapping("/subscription")
    public String listSubscriptions(Model model) {
        List<Subscription> subscriptions = subscriptionRepository.findAll();
        subscriptions.sort(Comparator.comparing(Subscription::getId));
        model.addAttribute("subscriptions", subscriptions);
        model.addAttribute("pageTitle", "Подписки");
        return "subscription";
    }

    @GetMapping("/add-subscription")
    public String subscriptionAdd(Model model) {
        model.addAttribute("subscription", new Subscription());
        model.addAttribute("pageTitle", "Подписки");
        List<User> userList = userRepository.findAll();
        model.addAttribute("users", userList);
        List<Host> hostList = hostRepository.findAll();
        model.addAttribute("hosts", hostList);
        List<String> metricsList = RawData.getMetricFields();
        model.addAttribute("metrics", metricsList);
        return "edit-subscription";
    }

    @PostMapping("/save-subscription")
    public String saveSubscription(@ModelAttribute Subscription subscription) {
        System.out.println("POST /save-subscription/ "+subscription);
        subscriptionRepository.save(subscription);
        return "redirect:/subscription";
    }

    @GetMapping("/edit-subscription/{id}")
    public String editSubscriptionForm(@PathVariable Long id, Model model) {
        Subscription subscription = subscriptionRepository.findById(id).orElse(new Subscription());
        model.addAttribute("subscription", subscription);
        model.addAttribute("pageTitle", "Подписки");
        List<User> userList = userRepository.findAll();
        model.addAttribute("users", userList);
        List<Host> hostList = hostRepository.findAll();
        model.addAttribute("hosts", hostList);
        List<String> metricsList = RawData.getMetricFields();
        model.addAttribute("metrics", metricsList);
        return "edit-subscription";
    }

    @GetMapping("/edit-subscription/{id}/{action}")
    public String editSubscription(@PathVariable Long id, @PathVariable String action, RedirectAttributes redirectAttributes) {
        System.out.println("POST /edit-subscription/"+id+"/"+action);

        Subscription existingSubscription = subscriptionRepository.findById(id).orElse(null);

        if (existingSubscription == null) {
            // Обработка случая, если подписка не найдена
            redirectAttributes.addFlashAttribute("errorMessage", "Подписка не найдена!");
            return "redirect:/subscription";
        }

        if (action.equals("enable")) {
            System.out.println("isActive=>true");
            existingSubscription.setActive(true);
            subscriptionRepository.save(existingSubscription);
            redirectAttributes.addFlashAttribute("successMessage", "Отправка уведомления включена");
        } else if (action.equals("disable")) {
            System.out.println("isActive=>false");
            existingSubscription.setActive(false);
            subscriptionRepository.save(existingSubscription);
            redirectAttributes.addFlashAttribute("successMessage", "Отправка уведомления выключена");
        }

        return "redirect:/subscription";
    }



}
