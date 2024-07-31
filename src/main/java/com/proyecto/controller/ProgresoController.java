package com.proyecto.controller;

import com.proyecto.domain.Progreso;
import com.proyecto.domain.User;
import com.proyecto.service.ProgresoService;
import com.proyecto.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@SessionAttributes("user")
public class ProgresoController {

    @Autowired
    private ProgresoService progresoService;

    @Autowired
    private UserService userService;

    @GetMapping("/progreso/entrenador")
    public String progresoEntrenador(@ModelAttribute("user") User user, Model model) {
        List<User> clientes = userService.getUsersByRole("cliente");
        model.addAttribute("clientes", clientes);
        return "progreso_entrenador";
    }

    @GetMapping("/progreso/cliente")
    public String progresoCliente(@ModelAttribute("user") User user, Model model) {
        List<Progreso> progresos = progresoService.getProgresosByUserId(user.getId());
        model.addAttribute("progresos", progresos);
        return "progreso_cliente";
    }

    @PostMapping("/progreso/entrenador/add")
    public String addProgreso(@RequestParam Long userId, @RequestParam String fecha, @RequestParam int peso,
                              @RequestParam int imc, @RequestParam int altura, @RequestParam boolean objetivoCumplido) {
        Progreso progreso = new Progreso();
        progreso.setUser(userService.getUser(userId));
        progreso.setFecha(java.sql.Date.valueOf(fecha));
        progreso.setPeso(peso);
        progreso.setImc(imc);
        progreso.setAltura(altura);
        progreso.setObjetivoCumplido(objetivoCumplido);
        progresoService.save(progreso);
        return "redirect:/progreso/entrenador";
    }

    @PostMapping("/progreso/entrenador/update")
    public String updateProgreso(@RequestParam Long idProgreso, @RequestParam String fecha, @RequestParam int peso,
                                 @RequestParam int imc, @RequestParam int altura, @RequestParam boolean objetivoCumplido) {
        Progreso progreso = progresoService.getProgresoById(idProgreso);
        progreso.setFecha(java.sql.Date.valueOf(fecha));
        progreso.setPeso(peso);
        progreso.setImc(imc);
        progreso.setAltura(altura);
        progreso.setObjetivoCumplido(objetivoCumplido);
        progresoService.save(progreso);
        return "redirect:/progreso/entrenador";
    }

    @PostMapping("/progreso/entrenador/delete")
    public String deleteProgreso(@RequestParam Long idProgreso) {
        progresoService.delete(idProgreso);
        return "redirect:/progreso/entrenador";
    }
}
