package com.proyecto.service;

import com.proyecto.domain.Survey;
import java.util.List;

public interface SurveyService {
    List<Survey> getSurveys();
    Survey getSurvey(Long id);
    void save(Survey survey);
    void delete(Survey survey);
}
