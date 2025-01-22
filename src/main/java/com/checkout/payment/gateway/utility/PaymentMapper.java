package com.checkout.payment.gateway.utility;

import com.checkout.payment.gateway.enums.PaymentStatus;
import com.checkout.payment.gateway.model.BankRequest;
import com.checkout.payment.gateway.model.BankResponse;
import com.checkout.payment.gateway.model.PostPaymentRequest;
import com.checkout.payment.gateway.model.PostPaymentResponse;
import org.springframework.stereotype.Component;
import java.util.UUID;

@Component
public class PaymentMapper {

  public PostPaymentResponse createPostPaymentResponse(
      BankResponse bankResponse,
      PostPaymentRequest paymentRequest
  ) {
    return new PostPaymentResponse(
        UUID.randomUUID(),
        bankResponse.isAuthorized() ? PaymentStatus.AUTHORIZED : PaymentStatus.DECLINED,
        paymentRequest.getCardNumberLastFour(),
        paymentRequest.getExpiryMonth(),
        paymentRequest.getExpiryYear(),
        paymentRequest.getCurrency(),
        paymentRequest.getAmount()
    );
  }

  public BankRequest createBankRequest(PostPaymentRequest paymentGatewayRequest) {
    return new BankRequest(
        paymentGatewayRequest.getCardNumber(),
        paymentGatewayRequest.getFormattedExpiryDate(),
        paymentGatewayRequest.getCurrency(),
        paymentGatewayRequest.getAmount(),
        paymentGatewayRequest.getCvv()
    );
  }
}

