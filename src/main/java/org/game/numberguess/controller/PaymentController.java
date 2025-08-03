package org.game.numberguess.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.game.numberguess.dto.request.PaymentCompleteRequest;
import org.game.numberguess.dto.request.PaymentRequest;
import org.game.numberguess.dto.response.PaymentResponse;
import org.game.numberguess.service.IVnPayService;
import org.game.numberguess.utils.UserContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final IVnPayService vnPayService;
    private final UserContext userContext;

    @PostMapping("/create-payment")
    public ResponseEntity<PaymentResponse> createPayment(@Valid @RequestBody PaymentRequest request) {
        PaymentResponse response = vnPayService.createPaymentUrl(request);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/complete")
    public ResponseEntity<?> completePayment(@Valid @RequestBody PaymentCompleteRequest request) {
        String username = userContext.getCurrentUsername();
        vnPayService.updateTurns(request, username);
        return ResponseEntity.ok("Complete payment successful");
    }
} 