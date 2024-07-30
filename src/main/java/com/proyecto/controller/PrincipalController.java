package com.proyecto.controller;

import com.proyecto.domain.User;
import com.proyecto.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@SessionAttributes("user")
public class PrincipalController {

    @Autowired
    private UserServiceImpl userServiceImpl;

    @ModelAttribute("user")
    public User user() {
        return new User();
    }

    @GetMapping("/principal")
    public String principal(@ModelAttribute("user") User user, Model model) {
        model.addAttribute("user", user);
        System.out.println("-----------------------------------------------------------------------------------------------------"+ user);
        return "paginaprincipal";
    }

    @GetMapping("/update/{idUser}")
    public String update(@PathVariable long idUser, Model model) {
        User user = userServiceImpl.getUser(idUser);
        if (user != null) {
            model.addAttribute("user", user);
            return "Actualizar";
        } else {
            model.addAttribute("error", "Usuario no encontrado");
            return "redirect:/principal";
        }
    }

    @GetMapping("/horarios")
    public String horarios(Model model) {
        return "horarios";
    }
}

