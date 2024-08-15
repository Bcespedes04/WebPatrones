package com.proyecto.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.proyecto.domain.Rol;
import com.proyecto.domain.User;
import com.proyecto.service.FirebaseStorageService;
import com.proyecto.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
@SessionAttributes("user")
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private FirebaseStorageService firebaseStorageService;

    @ModelAttribute("user")
    public User user() {
        return new User();
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password, Model model) {
        User user = userService.validateUser(email, password);
        if (user != null) {
            model.addAttribute("user", user);

            // Guardar el user.id y el nombre en un archivo JSON
            guardarUserIdEnJson(user.getIdUsuario(), user.getNombreUsuario());

            return "redirect:/principal";
        } else {
            model.addAttribute("status", "error");
            model.addAttribute("message", "Correo electrónico o contraseña inválidos");
            return "login";
        }
    }

    @GetMapping("/register")
    public String showRegister() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String email, @RequestParam String password, @RequestParam String confirmPassword, Model model) {
        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Las contraseñas no coinciden");
            return "register";
        }

        if (userService.userExists(email)) {
            model.addAttribute("error", "El correo electrónico ya está en uso");
            return "register";
        }

        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setActivo(true);

        Rol rol = new Rol();
        rol.setIdRol(2); // Asumiendo que 2 es el rol de cliente
        user.setRol(rol);

        userService.save(user);
        return "redirect:/login";
    }

    @GetMapping("/editDatos")
    public String showData() {
        return "usuario/editDatos";
    }

    @PostMapping("/guardar")
    public String usuarioGuardar(User user,
                                 @RequestParam("imagenFile") MultipartFile imagenFile) {

        User usuarioExistente = userService.getUser(user.getIdUsuario());

        if (usuarioExistente != null) {

            usuarioExistente.setNombreUsuario(user.getNombreUsuario());
            usuarioExistente.setGenero(user.getGenero());
            usuarioExistente.setDireccion(user.getDireccion());
            usuarioExistente.setCiudad(user.getCiudad());
            usuarioExistente.setPais(user.getPais());
            usuarioExistente.setZipCodigo(user.getZipCodigo());
            usuarioExistente.setTelefono(user.getTelefono());
            usuarioExistente.setFechaNacimiento(user.getFechaNacimiento());

            if (!imagenFile.isEmpty()) {
                String rutaImagen = firebaseStorageService.cargaImagen(
                        imagenFile,
                        "user",
                        user.getIdUsuario());
                usuarioExistente.setRutaImagen(rutaImagen);
            }

            userService.save(usuarioExistente);
        }

        return "redirect:/actualizar";
    }

    @GetMapping("/logout")
    public String logout(SessionStatus status) {
        status.setComplete();
        return "redirect:/login";
    }

    // Método para guardar el user.id y el nombre en un archivo JSON
    private void guardarUserIdEnJson(Long userId, String nombreUsuario) {
        Map<String, Object> data = new HashMap<>();
        data.put("userId", userId);
        data.put("name", nombreUsuario);

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // Guardar en src/main/resources/static/userData.json
            objectMapper.writeValue(new File("src/main/resources/static/userData.json"), data);
        } catch (IOException e) {
            e.printStackTrace(); // Manejo básico de excepciones, podría mejorarse para producción
        }
    }
}
