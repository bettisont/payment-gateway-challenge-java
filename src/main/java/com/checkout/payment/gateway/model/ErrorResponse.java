package com.checkout.payment.gateway.model;

public class ErrorResponse {
  private String message;

  public ErrorResponse(String message) {
    this.message = message;
  }

  public ErrorResponse() {
  }

  public String getMessage() {
    return message;
  }

  @Override
  public String toString() {
    return "ErrorResponse{" +
        "message='" + message + '\'' +
        '}';
  }
}
