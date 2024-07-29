package com.proyecto.controller;

import com.proyecto.domain.Zumba;
import com.proyecto.service.ZumbaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ZumbaController {

    @Autowired
    private ZumbaService zumbaService;

    @GetMapping("/zumba")
    public String zumba(Model model) {
        List<Zumba> zumbaClasses = zumbaService.listAll()
                                                .stream()
                                                .filter(zumbaClass -> "Zumba".equalsIgnoreCase(zumbaClass.getClase()))
                                                .collect(Collectors.toList());
        model.addAttribute("zumbaClasses", zumbaClasses);
        return "zumba";
    }

    @GetMapping("/spinning")
    public String spinning(Model model) {
        List<Zumba> spinningClasses = zumbaService.listAll()
                                                  .stream()
                                                  .filter(zumbaClass -> "Spinning".equalsIgnoreCase(zumbaClass.getClase()))
                                                  .collect(Collectors.toList());
        model.addAttribute("spinningClasses", spinningClasses);
        return "spinning";
    }

    @GetMapping("/aerobicos")
    public String aerobicos(Model model) {
        List<Zumba> aerobicosClasses = zumbaService.listAll()
                                                   .stream()
                                                   .filter(zumbaClass -> "Aerobicos".equalsIgnoreCase(zumbaClass.getClase()))
                                                   .collect(Collectors.toList());
        model.addAttribute("aerobicosClasses", aerobicosClasses);
        return "aerobicos";
    }

    @PostMapping("/addZumbaClass")
    public String addZumbaClass(@RequestParam("id_usuario") int userId,
                                @RequestParam("clase") String clase,
                                @RequestParam("fecha") String fecha,
                                @RequestParam("horario") String horario, 
                                Model model) {
        return validateAndAddClass(userId, clase, fecha, horario, model, "Zumba");
    }

    @PostMapping("/addSpinningClass")
    public String addSpinningClass(@RequestParam("id_usuario") int userId,
                                   @RequestParam("clase") String clase,
                                   @RequestParam("fecha") String fecha,
                                   @RequestParam("horario") String horario, 
                                   Model model) {
        return validateAndAddClass(userId, clase, fecha, horario, model, "Spinning");
    }

    @PostMapping("/addAerobicosClass")
    public String addAerobicosClass(@RequestParam("id_usuario") int userId,
                                    @RequestParam("clase") String clase,
                                    @RequestParam("fecha") String fecha,
                                    @RequestParam("horario") String horario, 
                                    Model model) {
        return validateAndAddClass(userId, clase, fecha, horario, model, "Aerobicos");
    }

    private String validateAndAddClass(int userId, String clase, String fecha, String horario, Model model, String expectedClassName) {
        List<Zumba> classes = zumbaService.listAll()
                                          .stream()
                                          .filter(zumbaClass -> expectedClassName.equalsIgnoreCase(zumbaClass.getClase()))
                                          .collect(Collectors.toList());

        if (!clase.equalsIgnoreCase(expectedClassName)) {
            model.addAttribute("error", "Nombre de clase incorrecto. Debe ser '" + expectedClassName + "'.");
            model.addAttribute(expectedClassName.toLowerCase() + "Classes", classes);
            return expectedClassName.toLowerCase();
        }

        java.sql.Date sqlFecha = java.sql.Date.valueOf(fecha);
        java.sql.Time sqlHorario = java.sql.Time.valueOf(horario.length() == 5 ? horario + ":00" : horario);
        
        boolean classExists = classes.stream()
                                     .anyMatch(zumbaClass -> zumbaClass.getFecha().equals(sqlFecha) &&
                                                             zumbaClass.getHorario().equals(sqlHorario));
        
        if (classExists) {
            model.addAttribute("error", "Ya hay una clase a esta hora.");
            model.addAttribute(expectedClassName.toLowerCase() + "Classes", classes);
            return expectedClassName.toLowerCase();
        }

        return addClass(userId, clase, fecha, horario, model, expectedClassName);
    }

    private String addClass(int userId, String clase, String fecha, String horario, Model model, String expectedClassName) {
        try {
            if (horario.length() == 5) {
                horario = horario + ":00";
            }

            Zumba zumba = new Zumba();
            zumba.setIdUsuario(userId);
            zumba.setClase(expectedClassName);
            zumba.setFecha(java.sql.Date.valueOf(fecha));
            zumba.setHorario(java.sql.Time.valueOf(horario));
            zumba.setEstado(true);
            zumbaService.save(zumba);

            return "redirect:/" + expectedClassName.toLowerCase();
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", "Formato de horario incorrecto. Debe ser 'hh:mm'.");
            List<Zumba> classes = zumbaService.listAll()
                                              .stream()
                                              .filter(zumbaClass -> expectedClassName.equalsIgnoreCase(zumbaClass.getClase()))
                                              .collect(Collectors.toList());
            model.addAttribute(expectedClassName.toLowerCase() + "Classes", classes);
            return expectedClassName.toLowerCase();
        }
    }

    @PostMapping("/deleteZumbaClass")
    public String deleteZumbaClass(@RequestParam("id_reserva") int idReserva) {
        zumbaService.delete(idReserva);
        return "redirect:/zumba";
    }

    @PostMapping("/deleteSpinningClass")
    public String deleteSpinningClass(@RequestParam("id_reserva") int idReserva) {
        zumbaService.delete(idReserva);
        return "redirect:/spinning";
    }

    @PostMapping("/deleteAerobicosClass")
    public String deleteAerobicosClass(@RequestParam("id_reserva") int idReserva) {
        zumbaService.delete(idReserva);
        return "redirect:/aerobicos";
    }
}
