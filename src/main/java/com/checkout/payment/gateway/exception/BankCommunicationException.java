package com.checkout.payment.gateway.exception;

public class BankCommunicationException extends RuntimeException {

  public BankCommunicationException(String message) {
    super(message);
  }

}
