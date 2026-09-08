package com.tuanhv.tripgoapi.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
public class CurrentUserProvider {

    public Long getUserId() {
        return getCurrentUser().id();
    }

    public CurrentUser getCurrentUser() {
        Jwt jwt = getJwt();

        Number userId = jwt.getClaim("userId");

        if (userId == null) {
            throw new IllegalStateException(
                    "JWT is missing required claim: userId"
            );
        }

        return new CurrentUser(
                userId.longValue(),
                jwt.getSubject(),
                jwt.getClaimAsString("role")
        );
    }

    private Jwt getJwt() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || !(authentication.getPrincipal() instanceof Jwt jwt)) {
            throw new IllegalStateException(
                    "Authenticated JWT principal is required"
            );
        }

        return jwt;
    }
}
