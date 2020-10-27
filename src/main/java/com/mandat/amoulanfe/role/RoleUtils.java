package com.mandat.amoulanfe.role;

import org.springframework.security.core.Authentication;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class RoleUtils {
    public static final String SUPER_ADMIN_ROLE_NAME = "SUPER_ADMIN";
    public static final String ADMIN_ROLE_NAME = "ADMIN";
    public static final String USER_ROLE_NAME = "USER";

    public static List<Role> getAdminAndSuperAdminRoles() {
        return Arrays.asList(Role.ADMIN, Role.SUPER_ADMIN);
    }

    public static List<Role> getAllRoles() {
        return Arrays.asList(Role.USER, Role.ADMIN, Role.SUPER_ADMIN);
    }

    public static List<Role> getAdminRoles() {
        return Collections.singletonList(Role.ADMIN);
    }

    public static String[] buildRolesListToStringsList(List<Role> roles) {
        return roles.stream().map(Role::toString).toArray(String[]::new);
    }

    public static boolean hasOneRole(Authentication auth, List<Role> roles) {
        List<String> authorities = toAuthorities(roles);
        return hasOneAuthority(auth, authorities);
    }

    private static List<String> toAuthorities(List<Role> roles) {
        return roles.stream().map(Role::toAuthority).collect(Collectors.toList());
    }

    public static boolean hasOneAuthority(Authentication auth, List<String> authorities) {
        if (auth == null || !auth.isAuthenticated()) {
            return false;
        }
        return auth.getAuthorities()
                .stream()
                .anyMatch(grantedAuthority -> authorities.contains(grantedAuthority.getAuthority()));
    }

    private enum Role {
        SUPER_ADMIN, ADMIN, USER;

        //roles start with this prefix
        private static final String PREFIX = "";

        @Override
        public String toString() {
            return PREFIX + name();
        }

        public String toAuthority() {
            return "ROLE_" + toString();
        }
    }
}