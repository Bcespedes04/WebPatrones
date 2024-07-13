package com.proyecto.controller;

import com.proyecto.domain.User;
import com.proyecto.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password, Model model) {
        User user = userService.validateUser(email, password);
        if (user != null) {
            model.addAttribute("user", user);
            return "paginaprincipal"; 
        } else {
            model.addAttribute("error", "Invalid email or password");
            return "login";
        }
    }

    @GetMapping("/register")
    public String showRegister() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String email, @RequestParam String password, @RequestParam String confirmPassword, Model model) {
        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Las contraseñas no coinciden");
            return "register";
        }

        if (userService.userExists(email)) {
            model.addAttribute("error", "El correo electrónico ya está en uso");
            return "register";
        }

        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setFechaRegistro(new java.util.Date());
        user.setActivo(true);

        userService.save(user);
        return "redirect:/login";
    }
}











