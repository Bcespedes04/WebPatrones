package com.proyecto.service.impl;

import com.proyecto.dao.AsistenciaDao;
import com.proyecto.domain.Asistencia;
import com.proyecto.service.AsistenciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AsistenciaServiceImpl implements AsistenciaService {

    @Autowired
    private AsistenciaDao asistenciaDao;

    @Override
    public List<Asistencia> listAll() {
        return (List<Asistencia>) asistenciaDao.findAll();
    }

    @Override
    public Asistencia getById(Long id) {
        return asistenciaDao.findById(id).orElse(null);
    }

    @Override
    public void save(Asistencia asistencia) {
        asistenciaDao.save(asistencia);
    }

    @Override
    public void delete(Long id) {
        asistenciaDao.deleteById(id);
    }
}
