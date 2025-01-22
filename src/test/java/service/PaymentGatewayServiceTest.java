package service;

import com.checkout.payment.gateway.controller.BaseTest;
import com.checkout.payment.gateway.model.PostPaymentResponse;
import com.checkout.payment.gateway.repository.PaymentsRepository;
import com.checkout.payment.gateway.service.BankService;
import com.checkout.payment.gateway.service.PaymentGatewayService;
import com.checkout.payment.gateway.utility.PaymentMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PaymentGatewayServiceTest extends BaseTest {

  @InjectMocks
  private PaymentGatewayService paymentGatewayService;

  @Mock
  private BankService bankService;

  @Mock
  private PaymentMapper paymentMapper;

  @Mock
  PaymentsRepository paymentsRepository;

  @Test
  void test_process_payment() {
    when(bankService.processRequest(validBankRequest)).thenReturn(authorisedBankResponse);
    when(paymentMapper.createBankRequest(validPaymentRequest)).thenReturn(validBankRequest);
    when(paymentMapper.createPostPaymentResponse(authorisedBankResponse, validPaymentRequest)).thenReturn(validPaymentResponse);
    when(paymentsRepository.add(validPaymentResponse)).thenReturn(validPaymentResponse);

    PostPaymentResponse postPaymentResponse = paymentGatewayService.processPayment(validPaymentRequest);

    assertEquals(validPaymentRequest.getAmount(), postPaymentResponse.getAmount());
  }


//  TODO add more test cases in here to cover other routes, including exception throwing etc
}
