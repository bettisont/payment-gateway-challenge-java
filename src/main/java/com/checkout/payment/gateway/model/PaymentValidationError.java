package com.checkout.payment.gateway.model;

public class PaymentValidationError {

  private String field;
  private String voilatedRule;
  private String message;
  private Object rejectedValue;

  public PaymentValidationError(String field, String voilatedRule, String message,
      Object rejectedValue) {
    this.field = field;
    this.voilatedRule = voilatedRule;
    this.message = message;
    this.rejectedValue = rejectedValue;
  }

  public PaymentValidationError() {
  }


  public String getField() {
    return field;
  }

  public void setField(String field) {
    this.field = field;
  }

  public String getVoilatedRule() {
    return voilatedRule;
  }

  public void setVoilatedRule(String voilatedRule) {
    this.voilatedRule = voilatedRule;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public Object getRejectedValue() {
    return rejectedValue;
  }

  public void setRejectedValue(Object rejectedValue) {
    this.rejectedValue = rejectedValue;
  }

  @Override
  public String toString() {
    return "PaymentValidationError{" +
        "field='" + field + '\'' +
        ", voilatedRule='" + voilatedRule + '\'' +
        ", message='" + message + '\'' +
        ", rejectedValue=" + rejectedValue +
        '}';
  }
}
