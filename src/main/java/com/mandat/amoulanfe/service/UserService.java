package com.mandat.amoulanfe.service;

import com.mandat.amoulanfe.domain.User;
import com.mandat.amoulanfe.dto.UserSummary;
import com.mandat.amoulanfe.repository.UserRepository;
import com.mandat.amoulanfe.security.UserPrincipal;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Setter
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

    public List<User> findAllUsers() {
        return this.userRepository.findAll();
    }
}
