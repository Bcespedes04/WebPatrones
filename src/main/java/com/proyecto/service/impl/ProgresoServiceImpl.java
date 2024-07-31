package com.proyecto.service.impl;

import com.proyecto.dao.ProgresoDao;
import com.proyecto.domain.Progreso;
import com.proyecto.service.ProgresoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProgresoServiceImpl implements ProgresoService {

    @Autowired
    private ProgresoDao progresoDao;

    @Override
    public List<Progreso> getAllProgresos() {
        return (List<Progreso>) progresoDao.findAll();
    }

    @Override
    public List<Progreso> getProgresosByUserId(Long userId) {
        return progresoDao.findByUserId(userId);
    }

    @Override
    public Progreso getProgresoById(Long id) {
        return progresoDao.findById(id).orElse(null);
    }

    @Override
    public void save(Progreso progreso) {
        progresoDao.save(progreso);
    }

    @Override
    public void delete(Long id) {
        progresoDao.deleteById(id);
    }
}
