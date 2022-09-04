package ru.kata.spring.boot_security.demo.service;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dao.UserDao;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private UserDao userDao;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserDao userDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User getUserById(long id) {
        return userDao.getUserById(id);
    }

    @Override
    public User getUserByEmail(String email) {
        return userDao.getUserByEmail(email);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = getUserByEmail(email);

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        Hibernate.initialize(user.getRoles());

        return user;
    }

    @Override
    @Transactional
    public void deleteUser(long id) {
        User user = getUserById(id);
        if (user != null) {
            userDao.deleteUser(id);
        }
    }

    @Override
    @Transactional
    public void saveOrUpdateUser(User user) {
        Long id = user.getId();

        if (id == null) {
            // password for a new user
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        } else {
            // password for existing user
            if (user.getPassword().equals("")) {
                // if password field is empty, use existing password
                user.setPassword(getUserById(id).getPassword());
            } else {
                // otherwise, set encoded password from the field
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            }
        }

        userDao.saveOrUpdateUser(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }
}
