package ru.sigma.sigmmix.controllers;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.sigma.sigmmix.model.User;
import ru.sigma.sigmmix.model.User.RoleType;
import ru.sigma.sigmmix.repositories.UserRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Controller
public class UserController {

    @Value("${application.name}")
    private String applicationName;

    @ModelAttribute
    public void addCommonAttributes(Model model) {
        model.addAttribute("appName", applicationName);
    }

    @Autowired
    private UserRepository userRepository;

    // Добавляем админа по умолчанию при запуске приложения
    @PostConstruct
    private void addDefaultUser() {
        if (userRepository.findByLogin("admin") == null) {
            User defaultUser = new User("admin", "admin", RoleType.ADMIN);
            userRepository.save(defaultUser);
        }
    }


    @GetMapping("/users")
    public String listUsers(Model model) {
        List<User> users = userRepository.findAll();
        users.sort(Comparator.comparing(User::getId));
        model.addAttribute("users", users);
        model.addAttribute("pageTitle", "Пользователи");
        return "users";
    }

    @GetMapping("/add-user")
    public String userAdd(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("pageTitle", "Пользователи");
        return "edit-user";
    }

    @GetMapping("/edit-user/{id}")
    public String editUserForm(@PathVariable Long id, Model model) {
        User user = userRepository.findById(id).orElse(new User());
        model.addAttribute("user", user);
        model.addAttribute("pageTitle", "Пользователи");
        return "edit-user";
    }

    @PostMapping("/save-user")
    public String addUser(@ModelAttribute User user) {
        userRepository.save(user);
        return "redirect:/users";
    }

    /*
    @PostMapping("/edit-user/{id}")
    public String editUser(@PathVariable Long id, @ModelAttribute User editedUser) {
        User existingUser = userRepository.findById(id).orElse(null);

        if (existingUser == null) {
            // Обработка случая, если пользователь не найден
            return "redirect:/users"; // или другой URL
        }

        // Обновите поля существующего пользователя данными из editedUser
        existingUser.setLogin(editedUser.getLogin());
        existingUser.setPassword(editedUser.getPassword());
        existingUser.setRole(editedUser.getRole());
        existingUser.setTelegramId(editedUser.getTelegramId());

        userRepository.save(existingUser);
        return "redirect:/users";
    }

    @GetMapping("/delete-user/{id}")
    public String deleteUser(@PathVariable Long id) {
        User user = userRepository.findById(id).orElse(null);

        if (user == null) {
            // Обработка случая, если пользователь не найден
            return "redirect:/users"; // или другой URL
        }

        userRepository.delete(user);
        return "redirect:/users";
    }
     */

    // Для отладки
    @GetMapping("/allUsers")
    @ResponseBody
    public List<User> allUsers() {
        Iterable<User> iterableUsers = userRepository.findAll();
        List<User> userList = new ArrayList<>();
        iterableUsers.forEach(userList::add);
        return userList;
    }
}
