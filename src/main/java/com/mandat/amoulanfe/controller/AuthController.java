package com.mandat.amoulanfe.controller;

import com.mandat.amoulanfe.domain.User;
import com.mandat.amoulanfe.security.CurrentUser;
import com.mandat.amoulanfe.security.UserPrincipal;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.mandat.amoulanfe.dto.JwtAuthenticationResponse;
import com.mandat.amoulanfe.dto.LoginRequest;
import com.mandat.amoulanfe.dto.SignUpRequest;
import com.mandat.amoulanfe.service.AuthService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/v1/amoulanfe/user/")
public class AuthController {

    private AuthService authService;

    private static final String URL = "http://localhost:8000/api/v1/amoulanfe/signin";
    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @ApiOperation(value = "Permettre à un utilisateur de s'identifier")
    @PostMapping("/signin")
    @ResponseStatus(OK)
    public JwtAuthenticationResponse login(@Valid @RequestBody LoginRequest loginRequest) {
        return authService.authenticateUser(loginRequest);
    }

    @ApiOperation(value = "Création d'un compte")
    @PostMapping("/signup")
    @ResponseStatus(OK)
    public Long register(@Valid @RequestBody SignUpRequest signUpRequest) {
        User user = authService.registerUser(signUpRequest);
      //  authService.sendConfirmationEmail(URL, user);
        return user.getId();
    }
}
