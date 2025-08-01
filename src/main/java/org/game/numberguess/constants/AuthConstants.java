package org.game.numberguess.constants;

import lombok.Getter;

@Getter
public enum AuthConstants {

    // BCrypt configuration
    BCRYPT_STRENGTH(10);

    private final int value;

    AuthConstants(int value) {
        this.value = value;
    }
}
