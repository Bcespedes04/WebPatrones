package com.proyecto.controller;

import com.proyecto.domain.Survey;
import com.proyecto.domain.User;
import com.proyecto.service.SurveyService;
import com.proyecto.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/survey")
public class SurveyController {

    @Autowired
    private SurveyService surveyService;

    @Autowired
    private UserService userService;

    @GetMapping("/list")
    public String listSurveys(Model model) {
        List<Survey> surveys = surveyService.getSurveys();
        model.addAttribute("surveys", surveys);
        return "list";
    }

    @GetMapping("/new")
    public String newSurvey(Model model) {
        model.addAttribute("survey", new Survey());
        List<User> users = userService.getUsers(true);
        model.addAttribute("users", users);
        return "form";  
    }

    @PostMapping("/save")
    public String saveSurvey(@ModelAttribute Survey survey) {
        surveyService.save(survey);
        return "redirect:/survey/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteSurvey(@PathVariable Long id) {
        Survey survey = surveyService.getSurvey(id);
        if (survey != null) {
            surveyService.delete(survey);
        }
        return "redirect:/survey/list";
    }
}


