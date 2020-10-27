package com.mandat.amoulanfe.user;

import com.mandat.amoulanfe.security.CurrentUser;
import com.mandat.amoulanfe.security.UserPrincipal;
import io.swagger.annotations.ApiOperation;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public UserSummary getCurrentUser(@CurrentUser UserPrincipal currentUser) {
        return userService.getCurrentUser(currentUser);
    }

    @ApiOperation(value = "Retourne tous les utilisateurs")
    @GetMapping("all")
    public Page<User> findAll(Pageable pageable) {
        return userService.findAllUsers(pageable);
    }

}
