package org.game.numberguess.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.game.numberguess.dto.request.GuessRequest;
import org.game.numberguess.dto.response.GuessResponse;
import org.game.numberguess.dto.response.UserLeaderboard;
import org.game.numberguess.dto.response.UserProfileResponse;
import org.game.numberguess.service.IUserService;
import org.game.numberguess.utils.UserContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("")
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

    @PostMapping("/guess")
    public ResponseEntity<GuessResponse> guess(@RequestBody @Valid GuessRequest request) {
        String username = userContext.getCurrentUsername();
        GuessResponse response = userService.guess(username, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/leaderboard")
    public ResponseEntity<List<UserLeaderboard>> getLeaderboard() {
        String username = userContext.getCurrentUsername();
        return ResponseEntity.ok(userService.getTop10Users(username));
    }
}
