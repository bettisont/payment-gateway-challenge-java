package com.checkout.payment.gateway.service;

import com.checkout.payment.gateway.exception.InvalidPaymentDetailsException;
import com.checkout.payment.gateway.model.PaymentValidationError;
import com.checkout.payment.gateway.model.PostPaymentRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

//todo possibly make this into an 'validator' interface, which cardValidator can implement
@Service
public class CardDetailsValidatorService {

  private static final Logger LOG = LoggerFactory.getLogger(CardDetailsValidatorService.class);

  public CardDetailsValidatorService() {
  }

  public void validate(PostPaymentRequest paymentRequest) {
    LOG.info("validating payment {}", paymentRequest.toString());

    validateExpiryIsInFuture(paymentRequest.getExpiryYear(), paymentRequest.getExpiryMonth());
  }

  private List<PaymentValidationError> mapBindingResultToPaymentValidationObjects(BindingResult bindingResult) {
    List<PaymentValidationError> paymentValidationErrors = new ArrayList<>();
    bindingResult.getFieldErrors().forEach(fieldError -> paymentValidationErrors.add(
        new PaymentValidationError(
            fieldError.getField(),
            fieldError.getCode(),
            fieldError.getDefaultMessage(),
            fieldError.getRejectedValue()
        )
    ));
    return paymentValidationErrors;
  }

  public void handleValidationErrors(BindingResult bindingResult) {
    List<PaymentValidationError> paymentValidationErrors = mapBindingResultToPaymentValidationObjects(bindingResult);
    throw new InvalidPaymentDetailsException("Payment validation failure", paymentValidationErrors);
  }

  private void validateExpiryIsInFuture(Integer expiryYear, Integer expiryMonth) {
    LOG.info("verifying card expiry date is in future");
    YearMonth expiryDate = YearMonth.of(expiryYear, expiryMonth);

    if (!expiryDate.isAfter(YearMonth.now())) {
      throw new InvalidPaymentDetailsException("expiry date must be in the future.");
    }
  }

}
