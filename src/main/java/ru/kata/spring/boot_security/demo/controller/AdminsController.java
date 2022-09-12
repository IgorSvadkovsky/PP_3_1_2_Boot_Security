package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminsController {
    private UserService userService;
    private RoleService roleService;

    @Autowired
    public AdminsController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping()
    public String showAllUsers(ModelMap model, Principal principal) {
        User authenticatedUser = userService.getUserByEmail(principal.getName());
        List<User> allUsers = userService.getAllUsers();
        model.addAttribute("authenticatedUser", authenticatedUser);
        model.addAttribute("users", allUsers);
        model.addAttribute("allRoles", roleService.getAllRoles());
        return "admin";
    }

    @PostMapping()
    public String createOrUpdateUser(@ModelAttribute("user") User user, @RequestParam(value = "roleIds", required = false) String roleIds) {
        user.setRoles(userService.getRolesByIds(roleIds));
        userService.saveOrUpdateUser(user);
        return "redirect:/admin";
    }

    @PostMapping("/{id}")
    public String deleteUser(@PathVariable("id") int id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }

}