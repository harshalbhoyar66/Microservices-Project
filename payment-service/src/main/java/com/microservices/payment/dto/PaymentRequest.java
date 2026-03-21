package com.microservices.payment.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.antlr.v4.runtime.misc.NotNull;

@Data
public class PaymentRequest {

    @NotNull
    private Long orderId;

    @NotNull
    @Positive
    private Double amount;
}