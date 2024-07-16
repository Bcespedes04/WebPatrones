package com.proyecto.controller;

import com.proyecto.domain.User;
import com.proyecto.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class PrincipalController {

    @Autowired
    private UserServiceImpl userServiceImpl;

    @GetMapping("/principal")
    public String principal(Model model) {
        return "paginaprincipal";
    }

    @GetMapping("/update/{idUser}")
    public String update(@PathVariable long idUser, Model model) {
        // Busca el usuario utilizando el id proporcionado
        User user = userServiceImpl.getUser(idUser);
        if (user != null) {
            model.addAttribute("user", user);
            return "Actualizar";
        } else {
            model.addAttribute("error", "Usuario no encontrado");
            return "login";
        }
    }

}
