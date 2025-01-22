package com.checkout.payment.gateway.exception;

import com.checkout.payment.gateway.model.PaymentValidationError;
import java.util.List;

public class InvalidPaymentDetailsException extends RuntimeException{
  private final String message;
  List<PaymentValidationError> errors;

  public InvalidPaymentDetailsException(String message,
      List<PaymentValidationError> errors) {
    super(message);
    this.message = message;
    this.errors = errors;
  }

  public InvalidPaymentDetailsException(String message) {
    super(message);
    this.message = message;
  }

  @Override
  public String getMessage() {
    return message;
  }

  public List<PaymentValidationError> getErrors() {
    return errors;
  }

  public void setErrors(List<PaymentValidationError> errors) {
    this.errors = errors;
  }
}
