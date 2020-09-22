package com.example.paypaldemo.controllers;

import com.example.paypaldemo.model.PayoutOrder;
import com.example.paypaldemo.services.PayPalPaymentService;
import com.paypal.api.payments.PayoutBatch;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class PayPalPaymentController {
    @Autowired
    private PayPalPaymentService service;

    @RequestMapping(value = "/payout", method = RequestMethod.POST)
    public PayoutBatch payout(@RequestBody PayoutOrder payoutOrder) {
        try {
            return service.payout(payoutOrder.getTotalAmount(), payoutOrder.getCurrency(),
                    payoutOrder.getReceiver());
        } catch (PayPalRESTException e) {
            e.printStackTrace();
        }
        return null;
    }
}
