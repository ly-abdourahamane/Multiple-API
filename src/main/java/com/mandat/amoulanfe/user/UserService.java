package com.mandat.amoulanfe.user;

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
                .country(userPrincipal.getCountry())
                .city(userPrincipal.getCity())
                .district(userPrincipal.getDistrict())
                .roles(userPrincipal.getRoles())
                .build();
    }

    public List<User> findAllUsers() {
        return this.userRepository.findAllUsers();
    }
}
