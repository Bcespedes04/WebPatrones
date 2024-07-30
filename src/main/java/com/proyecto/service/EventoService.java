    package com.proyecto.service;

import com.proyecto.domain.Evento;
import java.util.List;

public interface EventoService {
    List<Evento> getEventos();
    Evento getEvento(Long id);
    void save(Evento evento);
    void delete(Long id);
}












