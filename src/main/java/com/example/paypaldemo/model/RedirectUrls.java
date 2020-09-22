package com.example.paypaldemo.model;

import lombok.Data;

@Data
public class RedirectUrls {
    public static final String SUCCESS_URL = "pay/success";
    public static final String CANCEL_URL = "pay/cancel";
}