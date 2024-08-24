// Interfaz UserService
package com.proyecto.service;

import com.proyecto.domain.User;
import java.util.List;

public interface UserService {
    User validateUser(String email, String password);
    List<User> getUsers(boolean active);
    User getUser(Long id);
    void save(User user);
    void delete(User user);
    boolean userExists(String email);

    // Métodos para la recuperación de contraseñas
    String initiatePasswordReset(String email);  // Cambiado a String
    User getUserByToken(String token);
    void updatePassword(User user, String newPassword);
}

