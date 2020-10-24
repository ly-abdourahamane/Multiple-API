package com.mandat.amoulanfe.user;

import com.mandat.amoulanfe.security.CurrentUser;
import com.mandat.amoulanfe.security.UserPrincipal;
import io.swagger.annotations.ApiOperation;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Setter
@RequestMapping("/api/v1/users")
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
    @PreAuthorize("hasRole('USER')")
    public List<User> findAll() {
        return userService.findAllUsers();
    }

}
