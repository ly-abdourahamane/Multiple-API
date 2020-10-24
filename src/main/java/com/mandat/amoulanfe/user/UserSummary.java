package com.mandat.amoulanfe.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Data
@AllArgsConstructor
@Builder
public class UserSummary {
    private Long id;
    private String name;
    private String email;
    private String country;
    private String city;
    private String district;
    private Collection<? extends GrantedAuthority> roles;
}
