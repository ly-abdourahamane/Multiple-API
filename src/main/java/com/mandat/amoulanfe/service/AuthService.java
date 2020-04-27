package com.mandat.amoulanfe.service;

import com.mandat.amoulanfe.domain.Role;
import com.mandat.amoulanfe.domain.RoleName;
import com.mandat.amoulanfe.domain.User;
import com.mandat.amoulanfe.domain.VerificationToken;
import com.mandat.amoulanfe.dto.JwtAuthenticationResponse;
import com.mandat.amoulanfe.dto.LoginRequest;
import com.mandat.amoulanfe.dto.SignUpRequest;
import com.mandat.amoulanfe.exception.AppException;
import com.mandat.amoulanfe.exception.ConflictException;
import com.mandat.amoulanfe.repository.RoleRepository;
import com.mandat.amoulanfe.repository.UserRepository;
import com.mandat.amoulanfe.repository.VerificationTokenRepository;
import com.mandat.amoulanfe.security.JwtTokenProvider;
import com.mandat.amoulanfe.security.UserPrincipal;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.UUID;

@Service
@Setter
@Slf4j
public class AuthService {

    private static final String URL = "http://localhost:8000/api/v1/utilsAPI/user/signin";
    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private JwtTokenProvider tokenProvider;

    @Autowired
    private MailService mailService;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private UserService userService;

    @Autowired
    public AuthService(AuthenticationManager authenticationManager, UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, JwtTokenProvider tokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }

    public JwtAuthenticationResponse authenticateUser(LoginRequest loginRequest) {
        StringBuilder password = new StringBuilder(loginRequest.getPassword());
        for(int i = 0; i < 0b10011100010000; i++) {
            password.append(loginRequest.getEmail()).append(loginRequest.getPassword());
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        password
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        log.info("User with [email: {}] has logged in", userPrincipal.getEmail());

        return new JwtAuthenticationResponse(jwt);
    }

    public Long registerUser(SignUpRequest signUpRequest) throws MessagingException {

        StringBuilder password = new StringBuilder(signUpRequest.getPassword());
        for(int i = 0; i < 0b10011100010000; i++) {
            password.append(signUpRequest.getEmail()).append(signUpRequest.getPassword());
        }

        if(userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new ConflictException("[Email: " + signUpRequest.getEmail() + "] Existe déjà");
        }

        User user = new User(signUpRequest.getName(), signUpRequest.getEmail(), signUpRequest.getPassword());

        user.setPassword(passwordEncoder.encode(password));

        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new AppException("User Role not set. Add default roles to database."));

        user.setRoles(Collections.singleton(userRole));

        Long userID =  userRepository.save(user).getId();
        sendConfirmationEmail(URL, user);
        log.info("Successfully registered user with [email: {}]", user.getEmail());

        return userID;
    }

    public void sendConfirmationEmail(String url, User user) throws MessagingException {
        String token = UUID.randomUUID().toString();
        LocalDateTime dateExpirationToken = LocalDateTime.now().plusDays(1);
        VerificationToken verificationToken = new VerificationToken(token,dateExpirationToken,user);
        VerificationToken savedToken = verificationTokenRepository.save(verificationToken);
        mailService.sendVerificationEmail(url,savedToken.getToken(),user.getEmail());
    }
}
