package ru.kata.spring.boot_security.demo.dao;

import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserDao {
    User getUserById(long id);
    User getUserByEmail(String email);
    void deleteUser(long id);
    void saveOrUpdateUser(User user);
    List<User> getAllUsers();
}
