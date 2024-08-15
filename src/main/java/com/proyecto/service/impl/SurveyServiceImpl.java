package com.proyecto.service.impl;

import com.proyecto.dao.SurveyDao;
import com.proyecto.domain.Survey;
import com.proyecto.service.SurveyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class SurveyServiceImpl implements SurveyService {

    @Autowired
    private SurveyDao surveyDao;

    @Override
    @Transactional(readOnly = true)
    public List<Survey> getSurveys() {
        return surveyDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Survey getSurvey(Long id) {
        return surveyDao.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void save(Survey survey) {
        surveyDao.save(survey);
    }

    @Override
    @Transactional
    public void delete(Survey survey) {
        surveyDao.delete(survey);
    }
}

