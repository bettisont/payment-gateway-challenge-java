package com.checkout.payment.gateway.controller;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.checkout.payment.gateway.enums.PaymentStatus;
import com.checkout.payment.gateway.model.PostPaymentRequest;
import com.checkout.payment.gateway.model.PostPaymentResponse;
import com.checkout.payment.gateway.repository.PaymentsRepository;
import java.util.UUID;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class PaymentGatewayControllerTest extends BaseTest {

  private static final Logger log = LoggerFactory.getLogger(PaymentGatewayControllerTest.class);
  @Autowired
  private MockMvc mvc;

  @Autowired
  PaymentsRepository paymentsRepository;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  void whenPaymentWithIdExistThenCorrectPaymentIsReturned() throws Exception {
    PostPaymentResponse payment = new PostPaymentResponse();
    payment.setId(UUID.randomUUID());
    payment.setAmount(10);
    payment.setCurrency("USD");
    payment.setStatus(PaymentStatus.AUTHORIZED);
    payment.setExpiryMonth(12);
    payment.setExpiryYear(2024);
    payment.setCardNumberLastFour(4321);

    paymentsRepository.add(payment);

    String contentAsString = mvc.perform(get("/payment/" + payment.getId()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value(payment.getStatus().getName()))
        .andExpect(jsonPath("$.cardNumberLastFour").value(payment.getCardNumberLastFour()))
        .andExpect(jsonPath("$.expiryMonth").value(payment.getExpiryMonth()))
        .andExpect(jsonPath("$.expiryYear").value(payment.getExpiryYear()))
        .andExpect(jsonPath("$.currency").value(payment.getCurrency()))
        .andExpect(jsonPath("$.amount").value(payment.getAmount())).andReturn().getResponse()
        .getContentAsString();

    System.out.println(contentAsString);
  }

  @Test
  void whenPaymentRequest_NoCvvThenRejectedReturned() throws Exception {

    validPaymentRequest.setCvv(null);

    String jsonRequest = objectMapper.writeValueAsString(validPaymentRequest);

    String result = mvc.perform(post("/payment")
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonRequest))
        .andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString();

    System.out.println("Response body: " + result);

    assertEquals("\"Rejected\"", result);
  }


  @Test
  void testPaymentRequest_valid_authorised_payment() throws Exception {

    String jsonRequest = objectMapper.writeValueAsString(validPaymentRequest);
    System.out.println("jsonRequest: " + jsonRequest);

    String result = mvc.perform(post("/payment")
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonRequest))
        .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

    System.out.println("Response body: " + result);

    PostPaymentResponse response = objectMapper.readValue(result, PostPaymentResponse.class);
    assertEquals(PaymentStatus.AUTHORIZED, response.getStatus());
    assertEquals(validPaymentRequest.getCardNumberLastFour(), response.getCardNumberLastFour());
    assertEquals(validPaymentRequest.getExpiryYear(), response.getExpiryYear());
    assertEquals(validPaymentRequest.getCurrency(), response.getCurrency());
    assertEquals(validPaymentRequest.getAmount(), response.getAmount());
  }

  @Test
  void testPaymentRequest_valid_declined_payment() throws Exception {
    PostPaymentRequest request = new PostPaymentRequest();
    request.setAmount(60000);
    request.setCurrency("USD");
    request.setExpiryMonth(1);
    request.setExpiryYear(2026);
    request.setCvv("456");
    request.setCardNumber("2222405343248112");

    String jsonRequest = objectMapper.writeValueAsString(request);
    System.out.println("jsonRequest: " + jsonRequest);

    String result = mvc.perform(post("/payment")
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonRequest))
        .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

    System.out.println("Response body: " + result);

    PostPaymentResponse response = objectMapper.readValue(result, PostPaymentResponse.class);
    assertEquals(PaymentStatus.DECLINED, response.getStatus());
    assertEquals(request.getCardNumberLastFour(), response.getCardNumberLastFour());
    assertEquals(request.getExpiryYear(), response.getExpiryYear());
    assertEquals(request.getCurrency(), response.getCurrency());
    assertEquals(request.getAmount(), response.getAmount());
  }



  @Test
  void whenPaymentWithIdDoesNotExistThen404IsReturned() throws Exception {
    mvc.perform(get("/payment/" + UUID.randomUUID()))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message").value("Invalid ID"));
  }

  @Test
  void whenDateInFutureThenRejected() throws Exception {
    validPaymentRequest.setExpiryYear(2022);
    validPaymentRequest.setExpiryMonth(10);

    String jsonRequest = objectMapper.writeValueAsString(validPaymentRequest);
    System.out.println("jsonRequest: " + jsonRequest);

    String result = mvc.perform(post("/payment")
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonRequest))
        .andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString();

    assertEquals("\"Rejected\"", result);
  }

  @Test
  void whenBankNotAvailableThenExceptionIsThrown() throws Exception {
    validPaymentRequest.setAmount(10);

    String jsonRequest = objectMapper.writeValueAsString(validPaymentRequest);
    System.out.println("jsonRequest: " + jsonRequest);

    String result = mvc.perform(post("/payment")
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonRequest)).andReturn().getResponse().getContentAsString();

//    todo verify exception
    System.out.println("result: " +result);
  }
}
