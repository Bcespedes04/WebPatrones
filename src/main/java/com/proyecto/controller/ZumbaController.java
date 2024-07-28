package com.proyecto.controller;

import com.proyecto.domain.Zumba;
import com.proyecto.service.ZumbaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.List;

@Controller
public class ZumbaController {

    @Autowired
    private ZumbaService zumbaService;

    @GetMapping("/zumba")
    public String zumba(Model model) {
        List<Zumba> zumbaClasses = zumbaService.listAll();
        model.addAttribute("zumbaClasses", zumbaClasses);
        return "zumba";
    }

    @PostMapping("/addZumbaClass")
    public String addZumbaClass(@SessionAttribute("userId") int userId,
                                @RequestParam("clase") String clase,
                                @RequestParam("fecha") String fecha,
                                @RequestParam("horario") String horario, 
                                Model model) {
        try {
            // Convertir "hh:mm" a "hh:mm:ss"
            if (horario.length() == 5) {
                horario = horario + ":00";
            }

            Zumba zumba = new Zumba();
            zumba.setIdUsuario(userId);  // Asignar el ID del usuario
            zumba.setClase(clase);
            zumba.setFecha(java.sql.Date.valueOf(fecha));
            zumba.setHorario(java.sql.Time.valueOf(horario));
            zumba.setEstado(true);
            zumbaService.save(zumba);
            return "redirect:/zumba";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", "Formato de horario incorrecto. Debe ser 'hh:mm'.");
            return "form";
        }
    }

    @PostMapping("/deleteZumbaClass")
    public String deleteZumbaClass(@RequestParam("id_reserva") int idReserva) {
        zumbaService.delete(idReserva);
        return "redirect:/zumba";
    }
}
