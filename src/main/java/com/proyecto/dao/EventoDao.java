package com.proyecto.dao;

import com.proyecto.domain.Evento;
import org.springframework.data.repository.CrudRepository;

public interface EventoDao extends CrudRepository<Evento, Long> {
}

