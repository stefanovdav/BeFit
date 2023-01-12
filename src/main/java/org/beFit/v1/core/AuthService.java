package org.beFit.v1.core;

import org.beFit.v1.core.models.Role;
import org.beFit.v1.core.models.User;
import org.beFit.v1.repositories.UserRepository;
import org.beFit.v1.repositories.entities.UserEntity;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.security.authentication.BadCredentialsException;

import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;

import static java.util.Optional.ofNullable;


public class AuthService {
    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String login(String username, String password) {
        Optional<UserEntity> user =
                userRepository.getUser(username);
        if (user.isEmpty() || !BCrypt.checkpw(password, user.get().passwordHash)) {
            throw new RuntimeException("invalid credentials");
        }


        return userRepository.createAuthToken(
                user.get().id, generateAuthToken());
    }

    public void register(String username, String password) {
        String passwordHash = BCrypt.hashpw(password, BCrypt.gensalt());
        List<Role> roles = List.of(Role.USER);
        userRepository.createUser(username, passwordHash, roles);
    }

    public Optional<User> getUserByAuthToken(String authToken) {
        String token = authToken;
        if(token.split(" ").length == 2) {
            token = removeBearer(token);
        }
        Optional<UserEntity> user =
                userRepository.getUserByAuthToken(token);
        return user.map(Mappers::fromUserEntity);
    }

    private String generateAuthTokenWithoutBearer() {
        int length = 16;
        String alphabet =
                "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder(length);
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < length; i++) {
            sb.append(alphabet.charAt(random.nextInt(alphabet.length())));
        }

        return sb.toString();
    }

    private String generateAuthToken() {
       return "Bearer " +  generateAuthTokenWithoutBearer();
    }

    public String removeBearer(String authToken) {
        return ofNullable(authToken)
                .map(value -> value.split(" ")[1]) // remove "Bearer "
                .map(String::trim)
                .orElseThrow(() -> new BadCredentialsException("authentication token is malformed"));
    }
}
