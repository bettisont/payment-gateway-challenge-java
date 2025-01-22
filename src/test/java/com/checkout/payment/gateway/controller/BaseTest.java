package com.checkout.payment.gateway.controller;

import com.checkout.payment.gateway.enums.PaymentStatus;
import com.checkout.payment.gateway.model.BankRequest;
import com.checkout.payment.gateway.model.BankResponse;
import com.checkout.payment.gateway.model.PostPaymentRequest;
import com.checkout.payment.gateway.model.PostPaymentResponse;
import org.junit.jupiter.api.BeforeEach;
import java.util.UUID;

public class BaseTest {

  protected PostPaymentRequest validPaymentRequest;
  protected PostPaymentResponse validPaymentResponse;
  protected BankResponse authorisedBankResponse;
  protected BankResponse unAuthorisedBankResponse;
  protected BankRequest validBankRequest;

  @BeforeEach
  void setup() {
    this.validPaymentRequest = new PostPaymentRequest(
        8877,
        "2222405343248877",
        4,
        2025,
        "GBP",
        100,
        "123"
    );

    this.validBankRequest = new BankRequest(
        this.validPaymentRequest.getCardNumber(),
        this.validPaymentRequest.getFormattedExpiryDate(),
        this.validPaymentRequest.getCurrency(),
        this.validPaymentRequest.getAmount(),
        this.validPaymentRequest.getCvv()
    );

    this.authorisedBankResponse = new BankResponse(true, "123");
    this.unAuthorisedBankResponse = new BankResponse(false, "123");

    this.validPaymentResponse = new PostPaymentResponse(
        UUID.randomUUID(),
        authorisedBankResponse.isAuthorized() ? PaymentStatus.AUTHORIZED : PaymentStatus.DECLINED,
        this.validPaymentRequest.getCardNumberLastFour(),
        this.validPaymentRequest.getExpiryMonth(),
        this.validPaymentRequest.getExpiryYear(),
        this.validPaymentRequest.getCurrency(),
        this.validPaymentRequest.getAmount()
    );
  }

}
