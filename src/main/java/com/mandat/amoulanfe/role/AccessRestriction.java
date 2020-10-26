package com.mandat.amoulanfe.role;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AccessRestriction {
    /**
     * @return The access level of the current user
     */
    public static AccessLevel getCurrentUserAccessLevel() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            //Super admin can access everything
            if (RoleUtils.hasOneRole(authentication, RoleUtils.getAdminAndSuperAdminRoles())) {
                return AccessLevel.ALL;
            }

            //Admin can access its assigned tasks
            if (RoleUtils.hasOneRole(authentication, RoleUtils.getAdminRoles())) {
                return AccessLevel.ASSIGNED;
            }
        }

        return AccessLevel.NONE;
    }
}