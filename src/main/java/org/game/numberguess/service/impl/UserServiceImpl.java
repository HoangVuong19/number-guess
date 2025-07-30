package org.game.numberguess.service.impl;

import lombok.RequiredArgsConstructor;
import org.game.numberguess.dto.response.LoginResponse;
import org.game.numberguess.dto.response.UserProfileResponse;
import org.game.numberguess.entity.User;
import org.game.numberguess.repository.IUserRepository;
import org.game.numberguess.service.IUserService;
import org.game.numberguess.utils.JwtUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {
    private final IUserRepository userRepository;
    private final JwtUtils jwtUtils;

    @Override
    public void usernameExists(String username) {
        if(userRepository.findByUsername(username).isPresent())
            throw new RuntimeException("Username already exists");
    }

    @Override
    public void register(String username, String password) {
        PasswordEncoder encoder = new BCryptPasswordEncoder(10);
        User user = User.builder()
                .username(username)
                .password(encoder.encode(password))
                .build();
        userRepository.save(user);
    }

    @Override
    public LoginResponse login(String username, String password) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
            throw new RuntimeException("Invalid username or password");
        }

        User user = userOptional.get();
        PasswordEncoder encoder = new BCryptPasswordEncoder(10);
        
        if (!encoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }

        var token = jwtUtils.generateToken(username);

        return LoginResponse.builder()
                .message("Login successful")
                .token(token)
                .build();
    }

    @Override
    public UserProfileResponse getUserProfile(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        User user = userOptional.get();
        return UserProfileResponse.builder()
                .email(user.getUsername())
                .score(user.getScore())
                .turns(user.getTurns())
                .build();
    }
}