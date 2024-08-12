package com.proyecto.service;

import com.proyecto.domain.Zumba;
import java.util.List;

public interface ZumbaService {
    List<Zumba> listAll();
    Zumba getById(int id);
    void save(Zumba zumba);
    void delete(int id);
}