package com.example.paypaldemo.services;

import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
public class PayPalPaymentService {

    private static final String INTENT = "sale";
    private static final String METHOD = "paypal";

    private final String SENDER_BATCH = "test sender batch";
    private final String EMAIL_SUBJECT = "test subject";
    private final String RECIPIENT_TYPE = "EMAIL";

    @Autowired
    private APIContext apiContext;

    public Payment createPayment(
            Double total,
            String currency,
            String description) throws PayPalRESTException {
        Amount amount = new Amount();
        amount.setCurrency(currency);
        total = new BigDecimal(total).setScale(2, RoundingMode.HALF_UP).doubleValue();
        amount.setTotal(String.format("%.2f", total));

        Transaction transaction = new Transaction();
        transaction.setDescription(description);
        transaction.setAmount(amount);

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        Payer payer = new Payer();
        payer.setPaymentMethod(METHOD);

        Payment payment = new Payment();
        payment.setIntent(INTENT);
        payment.setPayer(payer);
        payment.setTransactions(transactions);
        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(com.example.paypaldemo.model.RedirectUrls.CANCEL_URL);
        redirectUrls.setReturnUrl(com.example.paypaldemo.model.RedirectUrls.SUCCESS_URL);
        payment.setRedirectUrls(redirectUrls);

        return payment.create(apiContext);
    }

    public Payment executePayment(String paymentId, String payerId) throws PayPalRESTException {
        System.out.println("execute payment");
        Payment payment = new Payment();
        payment.setId(paymentId);
        PaymentExecution paymentExecute = new PaymentExecution();
        paymentExecute.setPayerId(payerId);
        return payment.execute(apiContext, paymentExecute);
    }

    public PayoutBatch payout(double total, String currency, String receiverEmail) throws PayPalRESTException {
        Date currentDate = new Date(System.currentTimeMillis());
        PayoutSenderBatchHeader payoutSenderBatchHeader = new PayoutSenderBatchHeader();
        payoutSenderBatchHeader.setSenderBatchId(SENDER_BATCH + " " + currentDate.toString());
        payoutSenderBatchHeader.setEmailSubject(EMAIL_SUBJECT);
        payoutSenderBatchHeader.setRecipientType(RECIPIENT_TYPE);
        List<PayoutItem> payoutItems = new ArrayList<>();

        payoutItems.add(new PayoutItem(new Currency(currency, String.format("%.2f", total)), receiverEmail));
        Payout payout = new Payout();

        payout.setSenderBatchHeader(payoutSenderBatchHeader);
        payout.setItems(payoutItems);

        return payout.create(apiContext, null);
    }
}
