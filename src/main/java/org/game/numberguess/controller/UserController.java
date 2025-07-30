package org.game.numberguess.controller;

import lombok.RequiredArgsConstructor;
import org.game.numberguess.dto.response.UserProfileResponse;
import org.game.numberguess.service.IUserService;
import org.game.numberguess.utils.UserContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;
    private final UserContext userContext;

    @GetMapping("/me")
    public ResponseEntity<UserProfileResponse> getMe() {
        String username = userContext.getCurrentUsername();
        UserProfileResponse userProfile = userService.getUserProfile(username);
        return ResponseEntity.ok(userProfile);
    }
}
