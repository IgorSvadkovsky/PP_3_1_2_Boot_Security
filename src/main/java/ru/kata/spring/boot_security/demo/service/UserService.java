package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserService extends UserDetailsService {
    User getUserById(long id);
    User getUserByEmail(String email);
    void deleteUser(long id);
    void saveOrUpdateUser(User user);
    List<User> getAllUsers();
}
