package com.checkout.payment.gateway.exception;

import com.checkout.payment.gateway.enums.PaymentStatus;
import com.checkout.payment.gateway.model.ErrorResponse;
import com.checkout.payment.gateway.model.PostPaymentResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.util.Objects;

//todo would be nice to make use of the Error Response object in here
@ControllerAdvice
public class CommonExceptionHandler {

  private static final Logger LOG = LoggerFactory.getLogger(CommonExceptionHandler.class);

  @ExceptionHandler(EventProcessingException.class)
  public ResponseEntity<ErrorResponse> handleException(EventProcessingException ex) {
    LOG.error("Exception happened", ex);
    return new ResponseEntity<>(new ErrorResponse(ex.getMessage()),
        HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(InvalidPaymentDetailsException.class)
  public ResponseEntity<PaymentStatus> handleException(InvalidPaymentDetailsException ex) {
    if (Objects.isNull(ex.getErrors()) || ex.getErrors().isEmpty()) {
      LOG.error("validation error: {}", ex.getMessage());
    } else {
//      here we log the errors, but do not return them, as defined in the requirements.
//      returning the errors could be a security concern
      ex.getErrors().forEach(error -> LOG.error("validation error on field '{}': {} (rejected value: '{}')",
          error.getField(),
          error.getMessage(),
          error.getRejectedValue()));
    }
    return ResponseEntity.badRequest().body(PaymentStatus.REJECTED);
  }

  @ExceptionHandler(BankCommunicationException.class)
  public ResponseEntity<String> handleException(BankCommunicationException ex) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
  }

  @ExceptionHandler(CardPaymentProcessingException.class)
  public ResponseEntity<String> handleException(CardPaymentProcessingException ex) {
    return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(String.format("%s: %s", ex.getMessage(), ex.getCause() != null ? ex.getCause().getMessage() : "no cause available"));
  }

}
