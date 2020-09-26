package org.gobiiproject.gobiiweb.controllers.gdm.v3;

import java.util.Optional;

import org.springframework.security.core.context.SecurityContextHolder;

public class AuthUtils {
    public static String getCurrentUser() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
            .map(auth -> auth.getName())
            .orElse(null);
    }
}
