package com.proyecto.dao;

import com.proyecto.domain.Survey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SurveyDao extends JpaRepository<Survey, Long> {
}

