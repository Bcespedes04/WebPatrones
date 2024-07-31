package com.proyecto.controller;

import com.proyecto.domain.Evento;
import com.proyecto.service.EventoService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class EventoController {
    
    @Autowired
    private EventoService eventoService;
    
    @GetMapping("/maraton")
    public String maraton(Model model) {
        List<Evento> maratonClasses = eventoService.getEventos()
                                                .stream()
                                                .filter(maratonClass -> "maraton".equalsIgnoreCase(maratonClass.getNombreEvento()))
                                                .collect(Collectors.toList());
        model.addAttribute("maratonClasses", maratonClasses);
        System.out.println("--------------------------------------------------------------------------------"+ maratonClasses);
        return "maraton";
    }
    
    @GetMapping("/levantamientopesas")
    public String levantamientopesas(Model model) {
        List<Evento> levantamientopesasClasses = eventoService.getEventos()
                                                  .stream()
                                                  .filter(maratonClass -> "levantamientopesas".equalsIgnoreCase(maratonClass.getNombreEvento()))
                                                  .collect(Collectors.toList());
        model.addAttribute("levantamientopesasClasses", levantamientopesasClasses);
        return "levantamientopesas";
    }
    
    @GetMapping("/resistencia")
    public String aerobicos(Model model) {
        List<Evento> resistenciaClasses = eventoService.getEventos()
                                                   .stream()
                                                   .filter(Evento -> "resistencia".equalsIgnoreCase(Evento.getNombreEvento()))
                                                   .collect(Collectors.toList());
        model.addAttribute("resistenciaClasses", resistenciaClasses);
        return "resistencia";
    }
    
    @PostMapping("/addmaraton")
    public String addmaratonClass(@RequestParam("user") int userId,
                                @RequestParam("nombreEvento") String clase,
                                @RequestParam("fecha") String fecha,
                                @RequestParam("horario") String horario, 
                                Model model) {
        return validateAndAddClass(userId, clase, fecha, horario, model, "Zumba");
    }
    
    
    
    private String validateAndAddClass(int userId, String clase, String fecha, String horario, Model model, String expectedClassName) {
        List<Evento> classes = eventoService.getEventos()
                                          .stream()
                                          .filter(Evento -> expectedClassName.equalsIgnoreCase(Evento.getNombreEvento()))
                                          .collect(Collectors.toList());

        if (!clase.equalsIgnoreCase(expectedClassName)) {
            model.addAttribute("error", "Nombre de clase incorrecto. Debe ser '" + expectedClassName + "'.");
            model.addAttribute(expectedClassName.toLowerCase() + "Classes", classes);
            return expectedClassName.toLowerCase();
        }

        java.sql.Date sqlFecha = java.sql.Date.valueOf(fecha);
        java.sql.Time sqlHorario = java.sql.Time.valueOf(horario.length() == 5 ? horario + ":00" : horario);
        
        boolean classExists = classes.stream()
                                     .anyMatch(Evento -> Evento.getFecha().equals(sqlFecha) &&
                                                             Evento.getHorario().equals(sqlHorario));
        
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

            Evento evento = new Evento();
            evento.setIdEvento(userId);
            evento.setNombreEvento(expectedClassName);
            evento.setFecha(java.sql.Date.valueOf(fecha));
            evento.setHorario(java.sql.Time.valueOf(horario));
            evento.setEstado(true);
            eventoService.save(evento);

            return "redirect:/" + expectedClassName.toLowerCase();
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", "Formato de horario incorrecto. Debe ser 'hh:mm'.");
            List<Evento> classes = eventoService.getEventos()
                                              .stream()
                                              .filter(Evento -> expectedClassName.equalsIgnoreCase(Evento.getNombreEvento()))
                                              .collect(Collectors.toList());
            model.addAttribute(expectedClassName.toLowerCase() + "Classes", classes);
            return expectedClassName.toLowerCase();
        }
    }
    
    @PostMapping("/deletemaraton")
    public String deletemaraton(@RequestParam("idEvento") Long idEvento) {
        eventoService.delete(idEvento);
        return "redirect:/maraton";
    }

    @PostMapping("/deletelevantamientopesas")
    public String deletelevantamientopesas(@RequestParam("idEvento") Long idEvento) {
        eventoService.delete(idEvento);
        return "redirect:/levantamientopesas";
    }

    @PostMapping("/deleteresistencia")
    public String deleteresistencia(@RequestParam("idEvento") Long idEvento) {
        eventoService.delete(idEvento);
        return "redirect:/resistencia";
    }
    
    
    
    
    
    
    

}
