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

    @ModelAttribute("user")
    public User user() {
        return new User();
    }

    @GetMapping("/progreso")
    public String mostrarProgreso(@ModelAttribute("user") User user, Model model) {
        if (user != null && user.getRol() != null) {
            String nombreRol = user.getRol().getNombreRol();
            if ("entrenador".equalsIgnoreCase(nombreRol)) {
                List<User> clientes = userService.getUsers(true);
                model.addAttribute("clientes", clientes);
                return "progreso_entrenador";
            } else {
                List<Progreso> progresos = progresoService.getProgresosByUserId(user.getIdUsuario());
                model.addAttribute("progresos", progresos);
                return "progreso_cliente";
            }
        }
        return "error"; 
    }

    @GetMapping("/progreso/add/{userId}")
    public String mostrarFormularioAgregar(@PathVariable Long userId, Model model) {
        User cliente = userService.getUser(userId);
        if (cliente != null) {
            model.addAttribute("cliente", cliente);
            model.addAttribute("progreso", new Progreso());
            return "agregar_progreso";
        }
        return "redirect:/progreso"; 
    }

@PostMapping("/progreso/add")
public String agregarProgreso(@ModelAttribute Progreso progreso, @RequestParam Long userId) {
    User cliente = userService.getUser(userId);
    if (cliente != null) {
        progreso.setUser(cliente);
        progreso.setIdProgreso(userId);  // Establecer id_progreso igual a id_usuario
        progreso.setImc(calcularIMC(progreso.getPeso(), progreso.getAltura()));
        progresoService.save(progreso);  // Guarda el progreso en la base de datos
    }
    return "redirect:/progreso";
}





    @GetMapping("/progreso/edit/{idProgreso}")
    public String mostrarFormularioModificar(@PathVariable Long idProgreso, Model model) {
        Progreso progreso = progresoService.getProgresoById(idProgreso);
        if (progreso != null) {
            model.addAttribute("progreso", progreso);
            return "modificar_progreso";
        }
        return "redirect:/progreso";
    }

    @PostMapping("/progreso/edit")
    public String modificarProgreso(@ModelAttribute Progreso progreso) {
        Progreso progresoExistente = progresoService.getProgresoById(progreso.getIdProgreso());
        if (progresoExistente != null) {
            progresoExistente.setFecha(progreso.getFecha());
            progresoExistente.setPeso(progreso.getPeso());
            progresoExistente.setAltura(progreso.getAltura());
            progresoExistente.setImc(calcularIMC(progreso.getPeso(), progreso.getAltura()));
            progresoExistente.setObjetivoCumplido(progreso.getObjetivoCumplido());
            progresoService.save(progresoExistente);
        }
        return "redirect:/progreso";
    }

    @GetMapping("/progreso/delete/{idProgreso}")
    public String mostrarFormularioEliminar(@PathVariable Long idProgreso, Model model) {
        Progreso progreso = progresoService.getProgresoById(idProgreso);
        if (progreso != null) {
            model.addAttribute("progreso", progreso);
            return "eliminar_progreso";
        }
        return "redirect:/progreso";
    }

    @PostMapping("/progreso/delete")
    public String eliminarProgreso(@RequestParam Long idProgreso) {
        progresoService.delete(idProgreso);
        return "redirect:/progreso";
    }

    private int calcularIMC(int peso, int altura) {
        if (altura > 0) {
            return (int) (peso / Math.pow(altura / 100.0, 2));
        }
        return 0;
    }
}
