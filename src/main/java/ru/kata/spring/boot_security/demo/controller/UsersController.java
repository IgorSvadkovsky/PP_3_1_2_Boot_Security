package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
//@RequestMapping("/users")
public class UsersController {
    private UserService userService;
    private RoleService roleService;

    @Autowired
    public UsersController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/admin")
    public String showAllUsers(ModelMap model) {
        List<User> allUsers = userService.getAllUsers();
//        List<Role> allRoles = roleService.getAllRoles();

        model.addAttribute("users", allUsers);
//        model.addAttribute("roles", allRoles);
//        System.out.println("ROOOOOLEEEEES: " + allRoles);

        return "admin";
    }

    @GetMapping("/admin/new")
    public String newUser(@ModelAttribute("user") User user, ModelMap model) {
        model.addAttribute("roles", roleService.getAllRoles());
        return "new";
    }

    @PostMapping("/admin")
    public String createOrUpdateUser(@ModelAttribute("user") User user) {
        user.setPassword((new BCryptPasswordEncoder()).encode(user.getPassword()));
        userService.saveOrUpdateUser(user);

        return "redirect:/admin";
    }

    @GetMapping("/admin/{id}/edit")
    public String editUser(@PathVariable("id") int id, ModelMap model) {
        model.addAttribute("user", userService.getUserById(id));
        model.addAttribute("roles", roleService.getAllRoles());
        return "new";
    }

    @PostMapping("/admin/{id}")
    public String deleteUser(@PathVariable("id") int id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }

    @GetMapping("/user")
    public String showUser(ModelMap model, Principal principal) {
        List<User> userList = new ArrayList<>();
        User user = userService.getUserByEmail(principal.getName());
        userList.add(user);
        model.addAttribute("users", userList);
        model.addAttribute("hideButtons", true);

        return "admin";
    }
}
