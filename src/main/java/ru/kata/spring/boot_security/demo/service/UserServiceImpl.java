package ru.kata.spring.boot_security.demo.service;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dao.RoleDao;
import ru.kata.spring.boot_security.demo.dao.UserDao;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private UserDao userDao;
    private RoleDao roleDao;
//    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
//        this.roleDao = roleDao;
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
//    @Transactional(propagation = Propagation.REQUIRED, readOnly = true, noRollbackFor = Exception.class)
    @Transactional
//    @Transactional(value = Transactional.TxType.REQUIRED)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//        User user = userDao.getUserByUsername(username);
        User user = getUserByEmail(email);

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        Hibernate.initialize(user.getRoles());
//        user.getAuthorities().size();
//        System.out.println("GET ROLES: " + user.getRoles());
//        user.getRoles();

        return user;
    }

//    @Override
//    public User getUserByUsername(String username) {
//        return userDao.getUserByUsername(username);
//    }

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
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userDao.saveOrUpdateUser(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }
}
