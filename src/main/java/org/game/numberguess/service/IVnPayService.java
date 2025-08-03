package org.game.numberguess.service;

import org.game.numberguess.dto.request.PaymentCompleteRequest;
import org.game.numberguess.dto.request.PaymentRequest;
import org.game.numberguess.dto.response.PaymentResponse;

public interface IVnPayService {
    PaymentResponse createPaymentUrl(PaymentRequest request);

    void updateTurns(PaymentCompleteRequest request, String username);
} 