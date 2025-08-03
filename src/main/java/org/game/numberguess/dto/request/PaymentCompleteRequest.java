package org.game.numberguess.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PaymentCompleteRequest {
    @NotNull(message = "Amount cannot be left blank")
    @Min(value = 10000, message = "Minimum amount is 10,000 VND")
    private Long amount;
}
