package ru.sigma.sigmmix.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import ru.sigma.sigmmix.model.Subscription;
import ru.sigma.sigmmix.repositories.SubscriptionRepository;

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

    @GetMapping("/subscription")
    public String listSubscriptions(Model model) {
        List<Subscription> subscriptions = subscriptionRepository.findAll();
        subscriptions.sort(Comparator.comparing(Subscription::getId));
        model.addAttribute("subscriptions", subscriptions);
        model.addAttribute("pageTitle", "Уведомления");
        return "subscription";
    }

}
