package com.checkout.payment.gateway.exception;

public class CardPaymentProcessingException extends RuntimeException {

  public CardPaymentProcessingException(String message, Throwable cause) {
    super(message, cause);
  }
}
