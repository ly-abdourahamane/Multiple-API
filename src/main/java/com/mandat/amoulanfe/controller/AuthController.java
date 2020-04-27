package com.mandat.amoulanfe.controller;

import com.mandat.amoulanfe.dto.JwtAuthenticationResponse;
import com.mandat.amoulanfe.dto.LoginRequest;
import com.mandat.amoulanfe.dto.SignUpRequest;
import com.mandat.amoulanfe.service.AuthService;
import io.swagger.annotations.ApiOperation;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@Setter
@RestController
@RequestMapping("/api/v1/utilsAPI/auth")
public class AuthController {

    private static final String URL = "http://localhost:8000/api/v1/amoulanfe/signin";
    @Autowired
    private AuthService authService;

    @PostMapping("/signin")
    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(OK)
    public JwtAuthenticationResponse login(@Valid @RequestBody LoginRequest loginRequest) {
        return authService.authenticateUser(loginRequest);
    }

    @ApiOperation(value = "Création d'un compte")
    @PostMapping("/signup")
    @ResponseStatus(CREATED)
    public Long register(@Valid @RequestBody SignUpRequest signUpRequest) throws MessagingException {
        return authService.registerUser(signUpRequest);
    }
}
