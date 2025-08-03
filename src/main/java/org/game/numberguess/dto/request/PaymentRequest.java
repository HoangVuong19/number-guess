package org.game.numberguess.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PaymentRequest {
    @NotNull(message = "Amount cannot be left blank")
    @Min(value = 10000, message = "Minimum amount is 10,000 VND")
    private Long amount;

    @NotBlank(message = "Order description cannot be left blank")
    private String orderInfo;

    @NotBlank(message = "Order code cannot be blank")
    private String orderId;

    private String bankCode;
} 