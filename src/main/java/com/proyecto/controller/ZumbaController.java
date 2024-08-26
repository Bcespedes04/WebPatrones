package com.proyecto.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.proyecto.domain.Zumba;
import com.proyecto.service.ZumbaService;
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

    @GetMapping("/asistencia")
    public String asistencia(Model model) {
        // Aquí puedes añadir lógica si necesitas enviar datos al modelo.
        return "asistencia"; // Nombre del template HTML en templates/asistencia.html
    }

    @PostMapping("/addZumbaClass")
    public String addZumbaClass(@RequestParam("clase") String clase,
            @RequestParam("fecha") String fecha,
            @RequestParam("horario") String horario,
            Model model) {
        int userId = cargarUserIdDesdeJson();  // Cargar el userId desde el JSON

        if (fecha == null || fecha.isEmpty()) {
            model.addAttribute("error", "La fecha es obligatoria.");
            model.addAttribute("showModal", true);
            return zumba(model);
        }
        if (horario == null || horario.isEmpty()) {
            model.addAttribute("error", "El horario es obligatorio.");
            model.addAttribute("showModal", true);
            return zumba(model);
        }
        return validateAndAddClass(userId, clase, fecha, horario, model, "Zumba");
    }

    @PostMapping("/addSpinningClass")
    public String addSpinningClass(@RequestParam("clase") String clase,
            @RequestParam("fecha") String fecha,
            @RequestParam("horario") String horario,
            Model model) {
        int userId = cargarUserIdDesdeJson();  // Cargar el userId desde el JSON

        if (fecha == null || fecha.isEmpty()) {
            model.addAttribute("error", "La fecha es obligatoria.");
            model.addAttribute("showModal", true);
            return spinning(model);
        }
        if (horario == null || horario.isEmpty()) {
            model.addAttribute("error", "El horario es obligatorio.");
            model.addAttribute("showModal", true);
            return spinning(model);
        }
        return validateAndAddClass(userId, clase, fecha, horario, model, "Spinning");
    }

    @PostMapping("/addAerobicosClass")
    public String addAerobicosClass(@RequestParam("clase") String clase,
            @RequestParam("fecha") String fecha,
            @RequestParam("horario") String horario,
            Model model) {
        int userId = cargarUserIdDesdeJson();  // Cargar el userId desde el JSON

        if (fecha == null || fecha.isEmpty()) {
            model.addAttribute("error", "La fecha es obligatoria.");
            model.addAttribute("showModal", true);
            return aerobicos(model);
        }
        if (horario == null || horario.isEmpty()) {
            model.addAttribute("error", "El horario es obligatorio.");
            model.addAttribute("showModal", true);
            return aerobicos(model);
        }
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
        java.sql.Time sqlHorario = convertStringToTime(horario);

        boolean classExists = classes.stream()
                .anyMatch(zumbaClass -> zumbaClass.getFecha().equals(sqlFecha)
                && zumbaClass.getHorario().equals(sqlHorario));

        if (classExists) {
            model.addAttribute("error", "Ya hay una clase a esta hora.");
            model.addAttribute(expectedClassName.toLowerCase() + "Classes", classes);
            return expectedClassName.toLowerCase();
        }

        return addClass(userId, clase, fecha, horario, model, expectedClassName);
    }

    private String addClass(int userId, String clase, String fecha, String horario, Model model, String expectedClassName) {
        try {
            Zumba zumba = new Zumba();
            zumba.setIdUsuario(userId);
            zumba.setClase(expectedClassName);
            zumba.setFecha(java.sql.Date.valueOf(fecha));
            zumba.setHorario(convertStringToTime(horario));
            zumba.setEstado(true);
            zumbaService.save(zumba);

            // Redireccionar a la página correcta basado en el nombre de la clase
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

    @PostMapping("/modifyZumbaClass")
    public String modifyZumbaClass(@RequestParam("id_reserva") int idReserva,
            @RequestParam("fecha") String fecha,
            @RequestParam("horario") String horario,
            Model model) {
        Zumba zumba = zumbaService.getById(idReserva);
        if (zumba != null) {
            try {
                zumba.setFecha(Date.valueOf(fecha));
                zumba.setHorario(convertStringToTime(horario));
                zumbaService.save(zumba);
            } catch (IllegalArgumentException e) {
                model.addAttribute("error", "Formato de horario incorrecto. Falta fecha o hora'.");
                model.addAttribute("showModifyModal", idReserva);
                model.addAttribute("zumbaClasses", zumbaService.listAll()
                        .stream()
                        .filter(zumbaClass -> "Zumba".equalsIgnoreCase(zumbaClass.getClase()))
                        .collect(Collectors.toList()));
                return "zumba";
            }
        }
        return "redirect:/zumba";
    }

    @PostMapping("/modifySpinningClass")
    public String modifySpinningClass(@RequestParam("id_reserva") int idReserva,
            @RequestParam("fecha") String fecha,
            @RequestParam("horario") String horario,
            Model model) {
        Zumba spinning = zumbaService.getById(idReserva);
        if (spinning != null) {
            try {
                spinning.setFecha(Date.valueOf(fecha));
                spinning.setHorario(convertStringToTime(horario));
                zumbaService.save(spinning);
            } catch (IllegalArgumentException e) {
                model.addAttribute("error", "Formato de horario incorrecto. Falta fecha o hora'.");
                model.addAttribute("showModifyModal", idReserva);
                model.addAttribute("spinningClasses", zumbaService.listAll()
                        .stream()
                        .filter(zumbaClass -> "Spinning".equalsIgnoreCase(zumbaClass.getClase()))
                        .collect(Collectors.toList()));
                return "spinning";
            }
        }
        return "redirect:/spinning";
    }

    @PostMapping("/modifyAerobicosClass")
    public String modifyAerobicosClass(@RequestParam("id_reserva") int idReserva,
            @RequestParam("fecha") String fecha,
            @RequestParam("horario") String horario,
            Model model) {
        Zumba aerobicos = zumbaService.getById(idReserva);
        if (aerobicos != null) {
            try {
                aerobicos.setFecha(Date.valueOf(fecha));
                aerobicos.setHorario(convertStringToTime(horario));
                zumbaService.save(aerobicos);
            } catch (IllegalArgumentException e) {
                model.addAttribute("error", "Formato de horario incorrecto. Falta fecha o hora'.");
                model.addAttribute("showModifyModal", idReserva);
                model.addAttribute("aerobicosClasses", zumbaService.listAll()
                        .stream()
                        .filter(zumbaClass -> "Aerobicos".equalsIgnoreCase(zumbaClass.getClase()))
                        .collect(Collectors.toList()));
                return "aerobicos";
            }
        }
        return "redirect:/aerobicos";
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