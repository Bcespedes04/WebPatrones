package com.proyecto.service.impl;

import com.proyecto.dao.ZumbaDao;
import com.proyecto.domain.Zumba;
import com.proyecto.service.ZumbaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ZumbaServiceImpl implements ZumbaService {

    @Autowired
    private ZumbaDao zumbaDao;

    @Override
    public List<Zumba> listAll() {
        return (List<Zumba>) zumbaDao.findAll();
    }

    @Override
    public Zumba getById(int id) {
        return zumbaDao.findById(id).orElse(null);
    }

    @Override
    public void save(Zumba zumba) {
        zumbaDao.save(zumba);
    }

    @Override
    public void delete(int id) {
        zumbaDao.deleteById(id);
    }
}
