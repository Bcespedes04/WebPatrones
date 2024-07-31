package com.proyecto.controller;

import com.proyecto.domain.User;
import com.proyecto.service.UserService;
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
    private UserService userService;

    @ModelAttribute("user")
    public User user() {
        return new User();
    }

@GetMapping("/principal")
public String principal(@ModelAttribute("user") User user, Model model) {
    if (user == null || user.getId() == null) {
        return "redirect:/login";
    }
    model.addAttribute("user", user);
    return "paginaprincipal";
}


    @GetMapping("/update/{idUser}")
    public String update(@PathVariable long idUser, Model model) {
        User user = userService.getUser(idUser);
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
    
    
@GetMapping("/progreso")
public String progreso(@ModelAttribute("user") User user) {
    if ("entrenador".equalsIgnoreCase(user.getRol().getNombreRol())) {
        return "redirect:/progreso/entrenador";
    } else {
        return "redirect:/progreso/cliente";
    }
}

}
