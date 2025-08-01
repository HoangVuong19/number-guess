package org.game.numberguess.constants;

import lombok.Getter;

@Getter
public enum GameConstants {
    
    // Game configuration
    INITIAL_SCORE(0),
    INITIAL_TURNS(5),
    SCORE_INCREMENT(1),
    TURN_DECREMENT(1),
    
    // Number guessing range
    MIN_NUMBER(1),
    MAX_NUMBER(5);

    private final int value;

    GameConstants(int value) {
        this.value = value;
    }
}