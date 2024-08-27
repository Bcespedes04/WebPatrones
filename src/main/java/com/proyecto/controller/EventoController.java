package com.proyecto.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.proyecto.domain.Evento;
import com.proyecto.service.EventoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;
import java.io.IOException;
import java.sql.Time;
import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class EventoController {

    @Autowired
    private EventoService eventoService;

    @GetMapping("/maraton")
    public String maraton(Model model) {
        List<Evento> maratonClasses = eventoService.getEventos()
                .stream()
                .filter(evento -> "Maraton".equalsIgnoreCase(evento.getNombreEvento()))
                .collect(Collectors.toList());
        model.addAttribute("maratonClasses", maratonClasses);
        return "maraton";
    }

    @GetMapping("/levantamientopesas")
    public String levantamientopesas(Model model) {
        List<Evento> levantamientopesasClasses = eventoService.getEventos()
                .stream()
                .filter(evento -> "Levantamiento de Pesas".equalsIgnoreCase(evento.getNombreEvento()))
                .collect(Collectors.toList());
        model.addAttribute("levantamientopesasClasses", levantamientopesasClasses);
        return "levantamientopesas";
    }

    @GetMapping("/resistencia")
    public String resistencia(Model model) {
        List<Evento> resistenciaClasses = eventoService.getEventos()
                .stream()
                .filter(evento -> "Resistencia".equalsIgnoreCase(evento.getNombreEvento()))
                .collect(Collectors.toList());
        model.addAttribute("resistenciaClasses", resistenciaClasses);
        return "resistencia";
    }

    @PostMapping("/addMaratonClass")
    public String addMaratonClass(@RequestParam("nombre_evento") String nombreEvento,
            @RequestParam("fecha") String fecha,
            @RequestParam("horario") String horario,
            Model model) {
        int userId = cargarUserIdDesdeJson();

        if (fecha == null || fecha.isEmpty()) {
            model.addAttribute("error", "La fecha es obligatoria.");
            model.addAttribute("showModal", true);
            return maraton(model);
        }
        if (horario == null || horario.isEmpty()) {
            model.addAttribute("error", "El horario es obligatorio.");
            model.addAttribute("showModal", true);
            return maraton(model);
        }
        return validateAndAddClass(userId, nombreEvento, fecha, horario, model, "Maraton");
    }

    @PostMapping("/addLevantamientoPesasClass")
    public String addLevantamientoPesasClass(@RequestParam("nombre_evento") String nombreEvento,
            @RequestParam("fecha") String fecha,
            @RequestParam("horario") String horario,
            Model model) {
        int userId = cargarUserIdDesdeJson();

        if (fecha == null || fecha.isEmpty()) {
            model.addAttribute("error", "La fecha es obligatoria.");
            model.addAttribute("showModal", true);
            return levantamientopesas(model);
        }
        if (horario == null || horario.isEmpty()) {
            model.addAttribute("error", "El horario es obligatorio.");
            model.addAttribute("showModal", true);
            return levantamientopesas(model);
        }
        return validateAndAddClass(userId, nombreEvento, fecha, horario, model, "Levantamiento de Pesas");
    }

    @PostMapping("/addResistenciaClass")
    public String addResistenciaClass(@RequestParam("nombre_evento") String nombreEvento,
            @RequestParam("fecha") String fecha,
            @RequestParam("horario") String horario,
            Model model) {
        int userId = cargarUserIdDesdeJson();

        if (fecha == null || fecha.isEmpty()) {
            model.addAttribute("error", "La fecha es obligatoria.");
            model.addAttribute("showModal", true);
            return resistencia(model);
        }
        if (horario == null || horario.isEmpty()) {
            model.addAttribute("error", "El horario es obligatorio.");
            model.addAttribute("showModal", true);
            return resistencia(model);
        }
        return validateAndAddClass(userId, nombreEvento, fecha, horario, model, "Resistencia");
    }

    private String validateAndAddClass(int userId, String clase, String fecha, String horario, Model model, String expectedClassName) {
        List<Evento> classes = eventoService.getEventos()
                .stream()
                .filter(evento -> expectedClassName.equalsIgnoreCase(evento.getNombreEvento()))
                .collect(Collectors.toList());

        if (!clase.equalsIgnoreCase(expectedClassName)) {
            model.addAttribute("error", "Nombre de clase incorrecto. Debe ser '" + expectedClassName + "'.");
            model.addAttribute(expectedClassName.toLowerCase() + "Classes", classes);
            return expectedClassName.toLowerCase();
        }

        java.sql.Date sqlFecha = java.sql.Date.valueOf(fecha);
        java.sql.Time sqlHorario = convertStringToTime(horario);

        boolean classExists = classes.stream()
                .anyMatch(evento -> evento.getFecha().equals(sqlFecha)
                && evento.getHorario().equals(sqlHorario));

        if (classExists) {
            model.addAttribute("error", "Ya hay una clase a esta hora.");
            model.addAttribute(expectedClassName.toLowerCase() + "Classes", classes);
            return expectedClassName.toLowerCase();
        }

        return addClass(userId, clase, fecha, horario, model, expectedClassName);
    }

    private String addClass(int userId, String clase, String fecha, String horario, Model model, String expectedClassName) {
        try {
            Evento evento = new Evento();
            evento.setIdUsuario(userId);
            evento.setNombreEvento(clase);
            evento.setFecha(java.sql.Date.valueOf(fecha));
            evento.setHorario(convertStringToTime(horario));
            evento.setEstado(true);
            eventoService.save(evento);

            return "redirect:/" + expectedClassName.toLowerCase();
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", "Formato de horario incorrecto. Debe ser 'hh:mm'.");
            List<Evento> classes = eventoService.getEventos()
                    .stream()
                    .filter(evento -> expectedClassName.equalsIgnoreCase(evento.getNombreEvento()))
                    .collect(Collectors.toList());
            model.addAttribute(expectedClassName.toLowerCase() + "Classes", classes);
            return expectedClassName.toLowerCase();
        }
    }

    @PostMapping("/deleteMaratonClass")
    public String deleteMaratonClass(@RequestParam("id_evento") int idEvento) {
        eventoService.delete(idEvento);
        return "redirect:/maraton";
    }

    @PostMapping("/deleteLevantamientoPesasClass")
    public String deleteLevantamientoPesasClass(@RequestParam("id_evento") int idEvento) {
        eventoService.delete(idEvento);
        return "redirect:/levantamientopesas";
    }

    @PostMapping("/deleteResistenciaClass")
    public String deleteResistenciaClass(@RequestParam("id_evento") int idEvento) {
        eventoService.delete(idEvento);
        return "redirect:/resistencia";
    }

    @PostMapping("/modifyMaratonClass")
    public String modifyMaratonClass(@RequestParam("id_evento") int idEvento,
            @RequestParam("fecha") String fecha,
            @RequestParam("horario") String horario,
            Model model) {
        Evento maraton = eventoService.getEvento(idEvento);
        if (maraton != null) {
            try {
                maraton.setFecha(Date.valueOf(fecha));
                maraton.setHorario(Time.valueOf(horario + ":00"));
                eventoService.save(maraton);
                model.addAttribute("successMessage", "La clase de maratón ha sido modificada exitosamente.");
            } catch (Exception e) {
                model.addAttribute("errorMessage", "Hubo un error al modificar la clase de maratón: " + e.getMessage());
            }
        } else {
            model.addAttribute("errorMessage", "No se encontró la clase de maratón especificada.");
        }
        return "redirect:/maraton";
    }

    @PostMapping("/modifyLevantamientoPesasClass")
    public String modifyLevantamientoPesasClass(@RequestParam("id_evento") int idEvento,
            @RequestParam("fecha") String fecha,
            @RequestParam("horario") String horario,
            Model model) {
        Evento levantamientopesas = eventoService.getEvento(idEvento); // Usar getEvento en lugar de getById
        if (levantamientopesas != null) {
            try {
                levantamientopesas.setFecha(Date.valueOf(fecha));
                levantamientopesas.setHorario(convertStringToTime(horario));
                eventoService.save(levantamientopesas);
            } catch (IllegalArgumentException e) {
                model.addAttribute("error", "Formato de horario incorrecto. Falta fecha o hora.");
                model.addAttribute("showModifyModal", idEvento);
                model.addAttribute("levantamientopesasClasses", eventoService.getEventos()
                        .stream()
                        .filter(evento -> "Levantamiento de Pesas".equalsIgnoreCase(evento.getNombreEvento()))
                        .collect(Collectors.toList()));
                return "levantamientopesas";
            }
        }
        return "redirect:/levantamientopesas";
    }

    @PostMapping("/modifyResistenciaClass")
    public String modifyResistenciaClass(@RequestParam("id_evento") int idEvento,
            @RequestParam("fecha") String fecha,
            @RequestParam("horario") String horario,
            Model model) {
        Evento resistencia = eventoService.getEvento(idEvento); // Usar getEvento en lugar de getById
        if (resistencia != null) {
            try {
                resistencia.setFecha(Date.valueOf(fecha));
                resistencia.setHorario(convertStringToTime(horario));
                eventoService.save(resistencia);
            } catch (IllegalArgumentException e) {
                model.addAttribute("error", "Formato de horario incorrecto. Falta fecha o hora.");
                model.addAttribute("showModifyModal", idEvento);
                model.addAttribute("resistenciaClasses", eventoService.getEventos()
                        .stream()
                        .filter(evento -> "Resistencia".equalsIgnoreCase(evento.getNombreEvento()))
                        .collect(Collectors.toList()));
                return "resistencia";
            }
        }
        return "redirect:/resistencia";
    }

    private Time convertStringToTime(String timeString) {
        try {
            if (timeString.length() == 5) {
                timeString = timeString + ":00";
            }
            return Time.valueOf(timeString);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Formato de horario incorrecto. Falta fecha o hora.");
        }
    }

    private int cargarUserIdDesdeJson() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            File file = new File("src/main/resources/static/userData.json");

            if (!file.exists()) {
                file = new File("userData.json");
            }

            if (file.exists()) {
                Map<String, Integer> data = objectMapper.readValue(file, Map.class);
                return data.get("userId");
            } else {
                throw new RuntimeException("El archivo userData.json no existe.");
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al leer el archivo userData.json", e);
        }
    }
}