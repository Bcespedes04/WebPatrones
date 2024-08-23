package com.proyecto.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.proyecto.domain.Evento;
import com.proyecto.service.EventoService;
import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.util.List;
import java.util.Map;
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
    public String resistencia(Model model) {
        List<Evento> resistenciaClasses = eventoService.getEventos()
                .stream()
                .filter(Evento -> "resistencia".equalsIgnoreCase(Evento.getNombreEvento()))
                .collect(Collectors.toList());
        model.addAttribute("resistenciaClasses", resistenciaClasses);
        return "resistencia";
    }

    @PostMapping("/addmaratonClass")
    public String addmaratonClass(
            @RequestParam("nombre_evento") String nombreEvento,
            @RequestParam("fecha") String fecha,
            @RequestParam("horario") String horario,
            Model model) {
        int userId = cargarUserIdDesdeJson(); // Cargar el userId desde el JSON
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

    @PostMapping("/addlevantamientopesasClass")
    public String addlevantamientopesasClass(@RequestParam("nombre_evento") String nombreEvento,
            @RequestParam("fecha") String fecha,
            @RequestParam("horario") String horario,
            Model model) {
        int userId = cargarUserIdDesdeJson();  // Cargar el userId desde el JSON

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
        return validateAndAddClass(userId, nombreEvento, fecha, horario, model, "levantamientopesas");
    }

    @PostMapping("/addresistenciaClass")
    public String addResistenciaClass(@RequestParam("nombre_evento") String nombreEvento,
            @RequestParam("fecha") String fecha,
            @RequestParam("horario") String horario,
            Model model) {
        int userId = cargarUserIdDesdeJson();  // Cargar el userId desde el JSON

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
                .anyMatch(Evento -> Evento.getFecha().equals(sqlFecha)
                && Evento.getHorario().equals(sqlHorario));

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

    @PostMapping("/deleteMaratonClass")
    public String deletemaraton(@RequestParam("idEvento") int idEvento) {
        eventoService.delete(idEvento);
        return "redirect:/maraton";
    }

    @PostMapping("/deletelevantamientopesas")
    public String deletelevantamientopesas(@RequestParam("id_evento") int idEvento) {
        eventoService.delete(idEvento);
        return "redirect:/levantamientopesas";
    }

    @PostMapping("/deleteresistenciaClass")
    public String deleteresistencia(@RequestParam("id_evento") int idEvento) {
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
                maraton.setHorario(convertStringToTime(horario));
                eventoService.save(maraton);
            } catch (IllegalArgumentException e) {
                model.addAttribute("error", "Formato de horario incorrecto. Falta fecha o hora'.");
                model.addAttribute("showModifyModal", idEvento);
                model.addAttribute("maratonClasses", eventoService.getEventos()
                        .stream()
                        .filter(maratonClass -> "maraton".equalsIgnoreCase(maratonClass.getNombreEvento()))
                        .collect(Collectors.toList()));
                return "maraton";
            }
        }
        return "redirect:/maraton";
    }

    @PostMapping("/modifylevantamientopesas")
    public String modifylevantamientopesasClass(@RequestParam("id_evento") int idEvento,
            @RequestParam("fecha") String fecha,
            @RequestParam("horario") String horario,
            Model model) {
        Evento levantamientopesas = eventoService.getEvento(idEvento);
        if (levantamientopesas != null) {
            try {
                levantamientopesas.setFecha(Date.valueOf(fecha));
                levantamientopesas.setHorario(convertStringToTime(horario));
                eventoService.save(levantamientopesas);
            } catch (IllegalArgumentException e) {
                model.addAttribute("error", "Formato de horario incorrecto. Falta fecha o hora'.");
                model.addAttribute("showModifyModal", idEvento);
                model.addAttribute("levantamientopesasClasses", eventoService.getEventos()
                        .stream()
                        .filter(levantamientopesasClass -> "Aerobicos".equalsIgnoreCase(levantamientopesasClass.getNombreEvento()))
                        .collect(Collectors.toList()));
                return "resistencia";
            }
        }
        return "redirect:/levantamientopesasClass";
    }

    @PostMapping("/modifyresistenciaClass")
    public String modifyResistenciaClass(@RequestParam("id_evento") int idEvento,
            @RequestParam("fecha") String fecha,
            @RequestParam("horario") String horario,
            Model model) {
        Evento resistencia = eventoService.getEvento(idEvento);
        if (resistencia != null) {
            try {
                resistencia.setFecha(Date.valueOf(fecha));
                resistencia.setHorario(convertStringToTime(horario));
                eventoService.save(resistencia);
            } catch (IllegalArgumentException e) {
                model.addAttribute("error", "Formato de horario incorrecto. Falta fecha o hora'.");
                model.addAttribute("showModifyModal", idEvento);
                model.addAttribute("resistenciaClasses", eventoService.getEventos()
                        .stream()
                        .filter(resistenciaClass -> "Resistencia".equalsIgnoreCase(resistenciaClass.getNombreEvento()))
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
            throw new IllegalArgumentException("Formato de horario incorrecto. Falta fecha o hora'.");
        }
    }

    // Método privado para cargar el userId desde un archivo JSON
    private int cargarUserIdDesdeJson() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // Intentar encontrar el archivo en varias ubicaciones
            File file = new File("src/main/resources/static/userData.json");

            if (!file.exists()) {
                file = new File("userData.json");  // Intentar en el directorio de trabajo
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
