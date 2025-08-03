package org.game.numberguess.service.impl;

import lombok.RequiredArgsConstructor;
import org.game.numberguess.config.VnPayConfig;
import org.game.numberguess.dto.request.PaymentCompleteRequest;
import org.game.numberguess.dto.request.PaymentRequest;
import org.game.numberguess.dto.response.PaymentResponse;
import org.game.numberguess.entity.User;
import org.game.numberguess.exception.NotFoundException;
import org.game.numberguess.repository.IUserRepository;
import org.game.numberguess.service.IVnPayService;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class VnPayServiceImpl implements IVnPayService {

    private final VnPayConfig vnPayConfig;
    private final IUserRepository userRepository;

    @Override
    public PaymentResponse createPaymentUrl(PaymentRequest request) {
        try {
            String vnp_Version = "2.1.0";
            String vnp_Command = "pay";
            String vnp_TxnRef = request.getOrderId();
            String vnp_IpAddr = "127.0.0.1";
            String vnp_TmnCode = vnPayConfig.getTmnCode();
            String vnp_Amount = String.valueOf(request.getAmount() * 100);

            Map<String, String> vnp_Params = new HashMap<>();
            vnp_Params.put("vnp_Version", vnp_Version);
            vnp_Params.put("vnp_Command", vnp_Command);
            vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
            vnp_Params.put("vnp_Amount", vnp_Amount);
            vnp_Params.put("vnp_CurrCode", vnPayConfig.getCurrencyCode());
            vnp_Params.put("vnp_BankCode", request.getBankCode() != null ? request.getBankCode() : "");
            vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
            vnp_Params.put("vnp_OrderInfo", request.getOrderInfo());
            vnp_Params.put("vnp_OrderType", vnPayConfig.getOrderType());
            vnp_Params.put("vnp_Locale", vnPayConfig.getLocale());
            vnp_Params.put("vnp_ReturnUrl", vnPayConfig.getReturnUrl());
            vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
            vnp_Params.put("vnp_CreateDate", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));

            List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
            Collections.sort(fieldNames);
            StringBuilder hashData = new StringBuilder();
            StringBuilder query = new StringBuilder();
            Iterator<String> itr = fieldNames.iterator();
            while (itr.hasNext()) {
                String fieldName = itr.next();
                String fieldValue = vnp_Params.get(fieldName);
                if ((fieldValue != null) && (!fieldValue.isEmpty())) {
                    //Build hash data
                    hashData.append(fieldName);
                    hashData.append('=');
                    hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                    //Build query
                    query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII));
                    query.append('=');
                    query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                    if (itr.hasNext()) {
                        query.append('&');
                        hashData.append('&');
                    }
                }
            }
            String queryUrl = query.toString();
            String vnp_SecureHash = hmacSHA512(vnPayConfig.getHashSecret(), hashData.toString());
            queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
            String paymentUrl = vnPayConfig.getUrl() + "?" + queryUrl;

            return new PaymentResponse(paymentUrl, request.getOrderId(), request.getAmount(),
                    "Generated payment URL successfully", true);

        } catch (Exception e) {
            return new PaymentResponse(null, request.getOrderId(), request.getAmount(),
                    "Error generating payment URL: " + e.getMessage(), false);
        }
    }

    @Override
    public void updateTurns(PaymentCompleteRequest request, String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
            throw new NotFoundException("User not found");
        }
        User user = userOptional.get();

        long amount = request.getAmount();
        int turnsToAdd = switch ((int) amount) {
            case 10_000 -> 5;
            case 20_000 -> 12;
            case 50_000 -> 35;
            case 100_000 -> 70;
            default -> throw new IllegalArgumentException("Invalid payment amount: " + amount);
        };
        user.setTurns(user.getTurns() + turnsToAdd);
        LocalDateTime updatedAt = LocalDateTime.now();
        user.setUpdatedAt(updatedAt);

        userRepository.save(user);
    }

    private String hmacSHA512(final String key, final String data) {
        try {
            if (key == null || data == null) {
                throw new NullPointerException();
            }
            final Mac hmac512 = Mac.getInstance("HmacSHA512");
            byte[] hmacKeyBytes = key.getBytes();
            final SecretKeySpec secretKey = new SecretKeySpec(hmacKeyBytes, "HmacSHA512");
            hmac512.init(secretKey);
            byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
            byte[] result = hmac512.doFinal(dataBytes);
            StringBuilder sb = new StringBuilder(2 * result.length);
            for (byte b : result) {
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString();

        } catch (Exception ex) {
            return "";
        }
    }
} 