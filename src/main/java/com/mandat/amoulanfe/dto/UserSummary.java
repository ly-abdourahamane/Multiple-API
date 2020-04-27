package com.mandat.amoulanfe.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class UserSummary {
    private Long id;
    private String name;
    private String email;
    private Collection<? extends GrantedAuthority> roles;
}
