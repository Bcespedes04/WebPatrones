package com.proyecto.service.impl;

import com.proyecto.dao.UserDao;
import com.proyecto.domain.User;
import com.proyecto.service.CorreoService;
import com.proyecto.service.UserService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private CorreoService correoService;

    @Override
    @Transactional
    public String initiatePasswordReset(String email) {  // Devuelve String
        User user = userDao.findByEmail(email);
        if (user != null) {
            String token = UUID.randomUUID().toString();
            user.setTokenRecuperacion(token);
            user.setFechaSolicitudRecuperacion(new java.sql.Date(System.currentTimeMillis()));
            userDao.save(user);

            String subject = "Password Reset Request";
            String contenidoHtml = "<p>Click the link below to reset your password:</p>"
                                 + "<a href='http://localhost:8080/reset-password?token=" + token + "'>Reset Password</a>";

            try {
                correoService.enviarCorreoHtml(user.getEmail(), subject, contenidoHtml);
            } catch (MessagingException e) {
                e.printStackTrace();
            }

            return token;  // Retorna el token
        }

        return null;  // Devuelve null si el usuario no fue encontrado
    }

    @Override
    public User getUserByToken(String token) {
        return userDao.findByTokenRecuperacion(token);
    }

    @Override
    public void updatePassword(User user, String newPassword) {
        user.setPassword(newPassword); // Es recomendable encriptar la contraseña antes de guardarla
        user.setTokenRecuperacion(null);
        user.setFechaSolicitudRecuperacion(null);
        userDao.save(user);
    }

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

    @Transactional(readOnly = true)
    public User getUser(long id) {
        return userDao.findById(id).orElse(null);
    }
}

