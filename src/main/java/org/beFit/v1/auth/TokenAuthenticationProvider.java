package org.beFit.v1.auth;
import org.beFit.v1.core.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.stream.Collectors;

@Component
final class TokenAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {
    @Autowired
    private AuthService authService;

    @Override
    protected void additionalAuthenticationChecks(
            final UserDetails d, final UsernamePasswordAuthenticationToken auth) {
    }

    @Override
    protected UserDetails retrieveUser(
            final String username,
            final UsernamePasswordAuthenticationToken authentication) {
        final String token = (String) authentication.getCredentials();
        Optional<org.beFit.v1.core.models.User> user =
                authService.getUserByAuthToken(token);

        return user.map((c) ->
                new User(c.username, "",
                        c.roles.stream().map(Authorities::fromRole)
                                .collect(Collectors.toList()))).
                orElseThrow(() -> new BadCredentialsException("user not found"));
    }
}
