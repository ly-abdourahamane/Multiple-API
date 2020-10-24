package com.mandat.amoulanfe.dto;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Data
public class UserSummaryView {
    private Long id;
    private String name;
    private String email;
    private String country;
    private String city;
    private String district;
    private Collection<? extends GrantedAuthority> roles;
}
