package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dao.UserDao;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {
    private UserDao userDao;
    private PasswordEncoder passwordEncoder;
    private RoleService roleService;

    @Autowired
    public UserServiceImpl(UserDao userDao, PasswordEncoder passwordEncoder, RoleService roleService) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
    }

    @Override
    public User getUserById(long id) {
        return userDao.findById(id).get();
    }

    @Override
    public User getUserByEmail(String email) {
        return userDao.findUserByEmail(email).get();
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = getUserByEmail(email);

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return user;
    }

    @Override
    @Transactional
    public void deleteUser(long id) {
        User user = getUserById(id);
        if (user != null) {
            userDao.deleteById(id);
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

        userDao.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userDao.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    @Override
    public Collection<Role> getRolesByIds(String idsString) {
        Collection<Role> roleSet = new HashSet<>();
        String[] idsArray;

        if (idsString != null) {
            idsArray = idsString.split(",");
            for (String id: idsArray) {
                roleSet.add(roleService.getRoleById(Long.parseLong(id)).get());
            }
        }

        return roleSet;
    }
}
