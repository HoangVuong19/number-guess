package org.game.numberguess.service.impl;

import lombok.RequiredArgsConstructor;
import org.game.numberguess.constants.AuthConstants;
import org.game.numberguess.constants.GameConstants;
import org.game.numberguess.dto.request.GuessRequest;
import org.game.numberguess.dto.response.GuessResponse;
import org.game.numberguess.dto.response.LoginResponse;
import org.game.numberguess.dto.response.UserLeaderboard;
import org.game.numberguess.dto.response.UserProfileResponse;
import org.game.numberguess.entity.User;
import org.game.numberguess.repository.IUserRepository;
import org.game.numberguess.service.IUserService;
import org.game.numberguess.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {
    private final IUserRepository userRepository;
    private final JwtUtils jwtUtils;

    @Value("${game.win-rate}")
    private double winRate;

    @Override
    public void usernameExists(String username) {
        if (userRepository.findByUsername(username).isPresent())
            throw new RuntimeException("Username already exists");
    }

    @Override
    public void register(String username, String password) {
        PasswordEncoder encoder = new BCryptPasswordEncoder(AuthConstants.BCRYPT_STRENGTH.getValue());
        User user = User.builder()
                .username(username)
                .password(encoder.encode(password))
                .score(GameConstants.INITIAL_SCORE.getValue())
                .turns(GameConstants.INITIAL_TURNS.getValue())
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
        PasswordEncoder encoder = new BCryptPasswordEncoder(AuthConstants.BCRYPT_STRENGTH.getValue());
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
        ValidateRequest valid = getValidateRequest(username);

        return UserProfileResponse.builder()
                .email(valid.user().getUsername())
                .score(valid.user().getScore())
                .turns(valid.user().getTurns())
                .updatedAt(valid.formattedUpdatedAt())
                .build();
    }

    @Override
    public GuessResponse guess(String username, GuessRequest request) {
        ValidateRequest valid = getValidateRequest(username);

        if (valid.formattedUpdatedAt != null && !valid.formattedUpdatedAt.equals(request.getUpdatedAt())) {
            throw new RuntimeException("User data is out of sync. Please refresh.");
        }

        if (valid.user().getTurns() <= 0) {
            throw new RuntimeException("No turns remaining");
        }

        Result result = getResult(request);

        valid.user().setTurns(valid.user().getTurns() - GameConstants.TURN_DECREMENT.getValue());
        if (result.isCorrect()) {
            valid.user().setScore(valid.user().getScore() + GameConstants.SCORE_INCREMENT.getValue());
        }
        LocalDateTime updatedAt = LocalDateTime.now();
        valid.user().setUpdatedAt(updatedAt);

        userRepository.save(valid.user());

        return GuessResponse.builder()
                .message(result.isCorrect() ? "Congratulations! You guessed correctly!" : "Wrong guess! Try again!")
                .isCorrect(result.isCorrect())
                .correctNumber(result.correctNumber())
                .remainingTurns(valid.user().getTurns())
                .currentScore(valid.user().getScore())
                .updatedAt(updatedAt.format(valid.formatter))
                .build();
    }

    @Override
    public List<UserLeaderboard> getTop10Users(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        return userRepository.findTop10Leaderboard();
    }

    private Result getResult(GuessRequest request) {
        Random random = new Random();
        int correctNumber;

        if (random.nextDouble() < winRate) {
            correctNumber = request.getGuess();
        } else {
            do {
                correctNumber = random.nextInt(GameConstants.MAX_NUMBER.getValue()) + 1;
            } while (correctNumber == request.getGuess());
        }
        boolean isCorrect = request.getGuess().equals(correctNumber);
        return new Result(correctNumber, isCorrect);
    }

    private record Result(int correctNumber, boolean isCorrect) {
    }

    private ValidateRequest getValidateRequest(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        User user = userOptional.get();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
        String formattedUpdatedAt = user.getUpdatedAt() != null
                ? user.getUpdatedAt().format(formatter)
                : null;
        return new ValidateRequest(user, formattedUpdatedAt, formatter);
    }

    private record ValidateRequest(User user, String formattedUpdatedAt, DateTimeFormatter formatter) {
    }
}