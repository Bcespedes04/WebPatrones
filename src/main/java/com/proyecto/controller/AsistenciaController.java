package com.proyecto.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.proyecto.domain.Asistencia;
import com.proyecto.domain.User;
import com.proyecto.domain.Zumba;
import com.proyecto.service.AsistenciaService;
import com.proyecto.service.ZumbaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.util.Map;

@Controller
public class AsistenciaController {

    @Autowired
    private AsistenciaService asistenciaService;

    @Autowired
    private ZumbaService zumbaService;

    @PostMapping("/registrarseZumba")
    public String registrarseZumba(@RequestParam("id_zumba") int idZumba, Model model) {
        try {
            Long userId = cargarUserIdDesdeJson();  // Cargar el userId desde el JSON

            Zumba zumbaClass = zumbaService.getById(idZumba);
            if (zumbaClass == null) {
                model.addAttribute("error", "Clase no encontrada.");
                return "redirect:/zumba";
            }

            Asistencia asistencia = new Asistencia();
            asistencia.setUsuario(new User());  // Inicializar el objeto User
            asistencia.getUsuario().setIdUsuario(userId); // Asignar el usuario
            asistencia.setClase(zumbaClass.getClase()); // Asignar la clase
            asistencia.setFecha(new Date(zumbaClass.getFecha().getTime())); // Convertir java.util.Date a java.sql.Date
            asistencia.setHorario(new Time(zumbaClass.getHorario().getTime())); // Convertir java.util.Date a java.sql.Time
            asistencia.setEstado(zumbaClass.isEstado()); // Establecer el estado de la reserva
            asistenciaService.save(asistencia);

            return "redirect:/zumba";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Hubo un problema al registrar la clase.");
            return "redirect:/zumba";
        }
    }

    @PostMapping("/registrarseSpinning")
    public String registrarseSpinning(@RequestParam("id_spinning") int idSpinning, Model model) {
        try {
            Long userId = cargarUserIdDesdeJson();  // Cargar el userId desde el JSON

            Zumba spinningClass = zumbaService.getById(idSpinning);
            if (spinningClass == null) {
                model.addAttribute("error", "Clase no encontrada.");
                return "redirect:/spinning";
            }

            Asistencia asistencia = new Asistencia();
            asistencia.setUsuario(new User());  // Inicializar el objeto User
            asistencia.getUsuario().setIdUsuario(userId); // Asignar el usuario
            asistencia.setClase(spinningClass.getClase()); // Asignar la clase
            asistencia.setFecha(new Date(spinningClass.getFecha().getTime())); // Convertir java.util.Date a java.sql.Date
            asistencia.setHorario(new Time(spinningClass.getHorario().getTime())); // Convertir java.util.Date a java.sql.Time
            asistencia.setEstado(spinningClass.isEstado()); // Establecer el estado de la reserva
            asistenciaService.save(asistencia);

            return "redirect:/spinning";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Hubo un problema al registrar la clase.");
            return "redirect:/spinning";
        }
    }

    @PostMapping("/registrarseAerobicos")
    public String registrarseAerobicos(@RequestParam("id_aerobicos") int idAerobicos, Model model) {
        try {
            Long userId = cargarUserIdDesdeJson();  // Cargar el userId desde el JSON

            Zumba aerobicosClass = zumbaService.getById(idAerobicos);
            if (aerobicosClass == null) {
                model.addAttribute("error", "Clase no encontrada.");
                return "redirect:/aerobicos";
            }

            Asistencia asistencia = new Asistencia();
            asistencia.setUsuario(new User());  // Inicializar el objeto User
            asistencia.getUsuario().setIdUsuario(userId); // Asignar el usuario
            asistencia.setClase(aerobicosClass.getClase()); // Asignar la clase
            asistencia.setFecha(new Date(aerobicosClass.getFecha().getTime())); // Convertir java.util.Date a java.sql.Date
            asistencia.setHorario(new Time(aerobicosClass.getHorario().getTime())); // Convertir java.util.Date a java.sql.Time
            asistencia.setEstado(aerobicosClass.isEstado()); // Establecer el estado de la reserva
            asistenciaService.save(asistencia);

            return "redirect:/aerobicos";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Hubo un problema al registrar la clase.");
            return "redirect:/aerobicos";
        }
    }

    // Método privado para cargar el userId desde un archivo JSON
    private Long cargarUserIdDesdeJson() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // Intentar encontrar el archivo en varias ubicaciones
            File file = new File("src/main/resources/static/userData.json");

            if (!file.exists()) {
                file = new File("userData.json");  // Intentar en el directorio de trabajo
            }

            if (file.exists()) {
                Map<String, Long> data = objectMapper.readValue(file, Map.class);
                return data.get("userId");
            } else {
                throw new RuntimeException("El archivo userData.json no existe.");
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al leer el archivo userData.json", e);
        }
    }
}


