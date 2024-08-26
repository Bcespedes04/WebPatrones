package com.proyecto.dao;

import com.proyecto.domain.Asistencia;
import org.springframework.data.repository.CrudRepository;

public interface AsistenciaDao extends CrudRepository<Asistencia, Long> {
}
