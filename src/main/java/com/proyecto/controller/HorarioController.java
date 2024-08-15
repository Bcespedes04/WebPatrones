package com.proyecto.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HorarioController {

    @GetMapping("/clases")
    public String clases(Model model) {
        return "clases";
    }
    
    @GetMapping("/competencias")
    public String clase(Model model) {
        return "competencias";
    }
}