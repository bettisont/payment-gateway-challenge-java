package testUtils;

import com.checkout.payment.gateway.model.PostPaymentRequest;

public class TestUtils {

  public TestUtils() {
  }

  public PostPaymentRequest getPostPaymentRequest() {
    return new PostPaymentRequest(1231,
        "23412431234221",
        23,12,"GBP",1000,"123");
  }

  private PostPaymentRequest getValidPayment() {
    return new PostPaymentRequest(
        1234,
        "123812341212345678",
        12,
        2025,
        "GBP",
        1000,
        "123"
    );
  }

}
