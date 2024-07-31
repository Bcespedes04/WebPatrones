package com.proyecto.service.impl;

import com.proyecto.dao.UserDao;
import com.proyecto.domain.User;
import com.proyecto.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    @Transactional(readOnly = true)
    public List<User> getUsers(boolean active) {
        var list = userDao.findAll();
        if (active) {
            list.removeIf(e -> !e.isActive());
        }
        return list;
    }

    @Transactional(readOnly = true)
    public User getUser(User user) {
        return userDao.findById(user.getId()).orElse(null);
    }

    @Override
    @Transactional
    public void save(User user) {
        userDao.save(user);
    }

    @Override
    @Transactional
    public void delete(User user) {
        userDao.delete(user);
    }

    @Override
    public User validateUser(String email, String password) {
        return userDao.findByEmailAndPassword(email, password);
    }

    @Override
    public User getUser(Long id) {
        return userDao.findById(id).orElse(null);
    }

    @Override
    public boolean userExists(String email) {
        return userDao.findByEmail(email) != null;
    }
    
    @Override
    public List<User> getUsersByRole(String role) {
        return userDao.findByRolNombreRol(role);
    }
}

