package org.beFit.v1.auth;
import org.beFit.v1.core.models.Role;
import org.springframework.security.core.GrantedAuthority;

public abstract class Authorities {
    static class UserAuthority implements GrantedAuthority {
        @Override
        public String getAuthority() {
            return Role.USER.name();
        }
    }

    static class AdminAuthority implements GrantedAuthority {
        @Override
        public String getAuthority() {
            return Role.ADMIN.name();
        }
    }

    static GrantedAuthority fromRole(Role r) {
        return switch (r) {
            case USER -> new UserAuthority();
            case ADMIN -> new AdminAuthority();
        };
    }
}
