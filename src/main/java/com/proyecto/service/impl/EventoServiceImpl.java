package com.proyecto.service.impl;
import com.proyecto.dao.EventoDao;
import com.proyecto.domain.Evento;
import com.proyecto.service.EventoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class EventoServiceImpl implements EventoService {
    @Autowired
    private EventoDao eventoDao;
    @Override
    public List<Evento> getEventos() {
        return (List<Evento>) eventoDao.findAll();
    }

    @Override
    public Evento getEvento(int id) {
        return eventoDao.findById(id).orElse(null);
    }

    @Override
    public void save(Evento evento) {
        eventoDao.save(evento);
    }

    public void delete(int id) {
        eventoDao.deleteById(id);
    }}