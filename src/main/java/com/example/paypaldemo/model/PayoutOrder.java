package com.example.paypaldemo.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Builder
public class PayoutOrder {
    private double totalAmount;
    private String currency;
    private String receiver;
}
