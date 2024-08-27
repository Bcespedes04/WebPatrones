    package com.proyecto.service;

import com.proyecto.domain.Evento;
import java.util.List;

public interface EventoService {
    List<Evento> getEventos();
    Evento getEvento(int id);
    void save(Evento evento);
    void delete(int id);
}












