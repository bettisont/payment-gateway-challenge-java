package com.checkout.payment.gateway.service;

import com.checkout.payment.gateway.exception.BankCommunicationException;
import com.checkout.payment.gateway.model.BankRequest;
import com.checkout.payment.gateway.model.BankResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class BankService {

  private final RestTemplate restTemplate;

  private static final Logger LOG = LoggerFactory.getLogger(BankService.class);


  @Value("${bank.simulator.url:http://localhost:8080/payments}")
  private String bankSimulatorUrl;

  public BankService(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

// todo could implement automatic retries here
  public BankResponse processRequest(BankRequest bankRequest) {
    LOG.info("Sending request to bank simulator: {}", sanitiseBankRequest(bankRequest));
    try {
      return restTemplate.postForObject(bankSimulatorUrl, bankRequest, BankResponse.class);
    } catch (Exception e) {
      LOG.error("failed to communicate with bank {}", e.getMessage());
      throw new BankCommunicationException("failed to communicate with bank");
    }
  }

  private String sanitiseBankRequest(BankRequest bankRequest) {
    return String.format("BankRequest(cardNumber=****%s, expiryDate=%s, currency=%s, amount=%d)",
        bankRequest.getCardNumber().substring(bankRequest.getCardNumber().length() - 4),
        bankRequest.getExpiryDate(),
        bankRequest.getCurrency(),
        bankRequest.getAmount());
  }

}

