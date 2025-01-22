package com.checkout.payment.gateway.controller;

import com.checkout.payment.gateway.model.PostPaymentRequest;
import com.checkout.payment.gateway.model.PostPaymentResponse;
import com.checkout.payment.gateway.service.CardDetailsValidatorService;
import com.checkout.payment.gateway.service.PaymentGatewayService;
import java.util.UUID;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("api")
@Tag(name = "payment gateway controller", description = "endpoints for payment processing and retrieval")
public class PaymentGatewayController {

  private final PaymentGatewayService paymentGatewayService;
  private final CardDetailsValidatorService cardDetailsValidatorService;

  public PaymentGatewayController(PaymentGatewayService paymentGatewayService,
      CardDetailsValidatorService cardDetailsValidatorService) {
    this.paymentGatewayService = paymentGatewayService;
    this.cardDetailsValidatorService = cardDetailsValidatorService;
  }

  @GetMapping("/payment/{id}")
  public ResponseEntity<PostPaymentResponse> getPostPaymentEventById(@PathVariable UUID id) {
    return new ResponseEntity<>(paymentGatewayService.getPaymentById(id), HttpStatus.OK);
  }

  @Operation(
      summary = "process a payment",
      description = "validates a payment, before then sending to the bank",
      responses = {
          @ApiResponse(responseCode = "200", description = "payment processed successfully"),
          @ApiResponse(responseCode = "400", description = "invalid payment details"),
          @ApiResponse(responseCode = "500", description = "server error")
      }
  )
  @PostMapping("/payment")
  public ResponseEntity<PostPaymentResponse> processPayment(
      @Valid @RequestBody PostPaymentRequest request,
      BindingResult bindingResult) {

    if (bindingResult.hasErrors()) {
      cardDetailsValidatorService.handleValidationErrors(bindingResult);
    }

    cardDetailsValidatorService.validate(request);

    PostPaymentResponse response = paymentGatewayService.processPayment(request);

    return ResponseEntity.ok(response);
  }


}
