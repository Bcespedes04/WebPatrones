package com.proyecto.service;

import com.proyecto.domain.Progreso;
import java.util.List;

public interface ProgresoService {
    List<Progreso> getAllProgresos();
    List<Progreso> getProgresosByUserId(Long userId);
    Progreso getProgresoById(Long id);
    void save(Progreso progreso);
    void delete(Long id);
}


