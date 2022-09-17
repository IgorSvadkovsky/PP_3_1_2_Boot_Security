package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api")
public class RESTController {
    private UserService userService;
    private RoleService roleService;

    @Autowired
    public RESTController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/users")
    public List<User> showAllUsers(ModelMap model, Principal principal) {
        return userService.getAllUsers();
    }

    @PostMapping("/users")
    public User createUser(@RequestBody User user) {
        System.out.println("****user.getRoles: " + user.getRoles());
//        user.setRoles(userService.getRolesByIds(roleIds));
        userService.saveOrUpdateUser(user);
        return user;
    }

    @PutMapping("/users")
    public User updateUser(@RequestBody User user) {
//        user.setRoles(userService.getRolesByIds(roleIds));
        userService.saveOrUpdateUser(user);
        return user;
    }

    @DeleteMapping("/users/{id}")
    public String deleteUser(@PathVariable("id") int id) {
        userService.deleteUser(id);
        return "User was deleted";
    }
}
