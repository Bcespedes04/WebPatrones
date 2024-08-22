package com.proyecto.dao;

import com.proyecto.domain.Progreso;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProgresoDao extends CrudRepository<Progreso, Long> {
    List<Progreso> findByUserId(Long userId);
}

