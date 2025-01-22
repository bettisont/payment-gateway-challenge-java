package com.checkout.payment.gateway.service;

import com.checkout.payment.gateway.exception.BankCommunicationException;
import com.checkout.payment.gateway.exception.CardPaymentProcessingException;
import com.checkout.payment.gateway.exception.EventProcessingException;
import com.checkout.payment.gateway.model.BankRequest;
import com.checkout.payment.gateway.model.BankResponse;
import com.checkout.payment.gateway.model.PostPaymentRequest;
import com.checkout.payment.gateway.model.PostPaymentResponse;
import com.checkout.payment.gateway.repository.PaymentsRepository;
import java.util.UUID;
import com.checkout.payment.gateway.utility.PaymentMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PaymentGatewayService {

  private static final Logger LOG = LoggerFactory.getLogger(PaymentGatewayService.class);

  private final PaymentsRepository paymentsRepository;
  private final PaymentMapper paymentMapper;
  private final BankService bankService;

  public PaymentGatewayService(PaymentsRepository paymentsRepository,
      PaymentMapper paymentMapper, BankService bankService) {
    this.paymentsRepository = paymentsRepository;
    this.paymentMapper = paymentMapper;
    this.bankService = bankService;
  }

  public PostPaymentResponse getPaymentById(UUID id) {
    LOG.debug("Requesting access to to payment with ID {}", id);
    return paymentsRepository.get(id).orElseThrow(() -> new EventProcessingException("Invalid ID"));
  }

  //  no need to validate incoming payment request as validation already done at higher level
  public PostPaymentResponse processPayment(PostPaymentRequest paymentGatewayRequest) {
    LOG.info("processing payment request for carding ending: {}",paymentGatewayRequest.getCardNumberLastFour());

    BankRequest bankRequest = paymentMapper.createBankRequest(paymentGatewayRequest);
    LOG.info("bank request created: {}",bankRequest);

    BankResponse bankResponse;
//    TODO would be nice to implement some kind of retry mechanism here
    try {
      bankResponse = bankService.processRequest(bankRequest);
      LOG.info("received response from bank: {}",bankResponse);
    } catch (BankCommunicationException e) {
      LOG.error("error during processing payment request for carding ending: {}. {}"
          ,paymentGatewayRequest.getCardNumberLastFour(), e.getMessage());
      throw new CardPaymentProcessingException("there was an error whilst processing the card payment", e);
    }

    PostPaymentResponse response = paymentMapper.createPostPaymentResponse(
        bankResponse, paymentGatewayRequest);

    return this.savePayment(response);
  }

  private PostPaymentResponse savePayment(PostPaymentResponse paymentResponse) {
    LOG.debug("saving payment with ID {}", paymentResponse.getId());
    return paymentsRepository.add(paymentResponse);
  }
}
