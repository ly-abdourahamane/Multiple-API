package com.mandat.amoulanfe.service;

import com.mandat.amoulanfe.domain.User;
import com.mandat.amoulanfe.repository.UserRepository;
import com.mandat.amoulanfe.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mandat.amoulanfe.dto.UserSummary;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserSummary getCurrentUser(UserPrincipal userPrincipal) {
        return UserSummary.builder()
                .id(userPrincipal.getId())
                .email(userPrincipal.getEmail())
                .name(userPrincipal.getName())
                .build();
    }

    public List<User> findAll() {
        return this.userRepository.findAll();
    }
}
