package com.proyecto.service;

import com.proyecto.domain.Asistencia;
import java.util.List;

public interface AsistenciaService {
    List<Asistencia> listAll();
    Asistencia getById(Long id);
    void save(Asistencia asistencia);
    void delete(Long id);
}
