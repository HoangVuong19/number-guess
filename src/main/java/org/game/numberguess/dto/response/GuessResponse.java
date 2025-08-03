package org.game.numberguess.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GuessResponse {
    private String message;
    private Boolean isCorrect;
    private Integer correctNumber;
    private Integer remainingTurns;
    private Integer currentScore;
    private String updatedAt;
} 