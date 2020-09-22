package com.example.paypaldemo.controllers;

import com.example.paypaldemo.model.Order;
import com.example.paypaldemo.model.RedirectUrls;
import com.example.paypaldemo.services.PayPalPaymentService;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class PaypalController {

    @Autowired
    PayPalPaymentService service;


    @GetMapping("/")
    public String home() {
        return "home";
    }

    @PostMapping("/pay")
    public String payment(@RequestBody Order order) {
        try {
            Payment payment = service.createPayment(order.getTotalAmount(), order.getCurrency(), order.getDescription());
            for(Links link:payment.getLinks()) {
                if(link.getRel().equals("approval_url")) {
                    return "redirect:"+link.getHref();
                }
            }

        } catch (PayPalRESTException e) {

            e.printStackTrace();
        }
        return "redirect:/";
    }

    @GetMapping(value = RedirectUrls.CANCEL_URL)
    public String cancelPay() {
        System.out.println("cancel");
        return "cancel";
    }

    @GetMapping(value = RedirectUrls.SUCCESS_URL)
    public String successPay(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId) {
        try {
            Payment payment = service.executePayment(paymentId, payerId);
            if (payment.getState().equals("approved")) {
                return "success";
            }
        } catch (PayPalRESTException e) {
        }
        return "redirect:/";
    }
}