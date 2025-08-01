package org.game.numberguess.service;

import org.game.numberguess.dto.request.GuessRequest;
import org.game.numberguess.dto.response.GuessResponse;
import org.game.numberguess.dto.response.LoginResponse;
import org.game.numberguess.dto.response.UserProfileResponse;

public interface IUserService {
    void usernameExists(String username);

    void register(String username, String password);

    LoginResponse login(String username, String password);

    UserProfileResponse getUserProfile(String username);

    GuessResponse guess(String username, GuessRequest request);
} 