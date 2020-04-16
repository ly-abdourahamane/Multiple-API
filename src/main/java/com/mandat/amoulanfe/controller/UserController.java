package com.mandat.amoulanfe.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.mandat.amoulanfe.dto.UserSummary;
import com.mandat.amoulanfe.domain.User;
import com.mandat.amoulanfe.security.CurrentUser;
import com.mandat.amoulanfe.security.UserPrincipal;
import com.mandat.amoulanfe.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/amoulanfe")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ApiOperation(value = "Récuperer l'utilisateur courant")
    @GetMapping("me")
    @PreAuthorize("hasRole('USER')")
    public UserSummary getCurrentUser(@CurrentUser UserPrincipal currentUser) {
        return userService.getCurrentUser(currentUser);
    }

    @ApiOperation(value = "Retourne tous les utilisateurs")
    @GetMapping("all")
    public List<User> findAll() {
        return userService.findAll();
    }

}
