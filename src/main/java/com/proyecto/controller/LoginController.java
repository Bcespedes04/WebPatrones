package com.proyecto.controller;

import com.proyecto.domain.User;
import com.proyecto.service.FirebaseStorageService;
import com.proyecto.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private FirebaseStorageService firebaseStorageService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password, Model model) {
        User user = userService.validateUser(email, password);
        if (user != null) {
            model.addAttribute("user", user);
            return "paginaprincipal";
        } else {
            model.addAttribute("error", "Invalid email or password");
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
        // Recuperar el usuario existente desde la base de datos
        User usuarioExistente = userService.getUser(user.getIdUsuario());

        if (usuarioExistente != null) {
            // Actualizar solo los campos permitidos
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

            // Guardar los cambios en el usuario
            userService.save(usuarioExistente);
        }
    //Arreglar
        return "redirect:/actualizar";
    }
}
