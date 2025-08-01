package org.game.numberguess.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GuessRequest {
    @NotNull(message = "Guess is required")
    @Min(value = 1, message = "Guess must be at least 1")
    @Max(value = 5, message = "Guess must be at most 5")
    private Integer guess;
} 