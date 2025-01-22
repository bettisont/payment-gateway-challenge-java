package utility;

import com.checkout.payment.gateway.controller.BaseTest;
import com.checkout.payment.gateway.enums.PaymentStatus;
import com.checkout.payment.gateway.model.BankRequest;
import com.checkout.payment.gateway.model.PostPaymentResponse;
import com.checkout.payment.gateway.utility.PaymentMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PaymentMapperTest extends BaseTest {

  private final PaymentMapper mapper = new PaymentMapper();

  @Test
  void test_create_payment_gateway_response_is_authorised() {

    PostPaymentResponse paymentGatewayResponse = mapper.createPostPaymentResponse(authorisedBankResponse,
        validPaymentRequest);

    assertEquals(PaymentStatus.AUTHORIZED, paymentGatewayResponse.getStatus());
    assertEquals(validPaymentRequest.getCardNumberLastFour(), paymentGatewayResponse.getCardNumberLastFour());
    assertEquals(validPaymentRequest.getExpiryMonth(), paymentGatewayResponse.getExpiryMonth());
    assertEquals(validPaymentRequest.getExpiryYear(), paymentGatewayResponse.getExpiryYear());
    assertEquals(validPaymentRequest.getCurrency(), paymentGatewayResponse.getCurrency());
    assertEquals(validPaymentRequest.getAmount(), paymentGatewayResponse.getAmount());
  }

  @Test
  void test_create_payment_gateway_response_is_declined() {

    PostPaymentResponse paymentGatewayResponse = mapper.createPostPaymentResponse(unAuthorisedBankResponse,
        validPaymentRequest);

    assertEquals(PaymentStatus.DECLINED, paymentGatewayResponse.getStatus());
    assertEquals(validPaymentRequest.getCardNumberLastFour(), paymentGatewayResponse.getCardNumberLastFour());
    assertEquals(validPaymentRequest.getExpiryMonth(), paymentGatewayResponse.getExpiryMonth());
    assertEquals(validPaymentRequest.getExpiryYear(), paymentGatewayResponse.getExpiryYear());
    assertEquals(validPaymentRequest.getCurrency(), paymentGatewayResponse.getCurrency());
    assertEquals(validPaymentRequest.getAmount(), paymentGatewayResponse.getAmount());
  }

  @Test
  void test_create_bank_request() {
    BankRequest bankRequest = mapper.createBankRequest(validPaymentRequest);

    assertEquals(validPaymentRequest.getCardNumber(), bankRequest.getCardNumber());
    assertEquals(validPaymentRequest.getFormattedExpiryDate(), bankRequest.getExpiryDate());
    assertEquals(validPaymentRequest.getCurrency(), bankRequest.getCurrency());
    assertEquals(validPaymentRequest.getAmount(), bankRequest.getAmount());
    assertEquals(validPaymentRequest.getCvv(), bankRequest.getCvv());
  }

}
