package com.proyecto.service;

import com.proyecto.domain.User;
import java.util.List;

public interface UserService {
    User validateUser(String email, String password);
    List<User> getUsers(boolean active);
    List<User> getUsersByRole(String role);
    User getUser(Long id);
    void save(User user);
    void delete(User user);
    boolean userExists(String email);
}

