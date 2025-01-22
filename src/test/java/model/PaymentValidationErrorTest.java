package model;

import com.checkout.payment.gateway.model.PaymentValidationError;
import org.junit.jupiter.api.Test;
import org.meanbean.test.BeanTester;

public class PaymentValidationErrorTest {

  @Test
  void testJavaBeanProperties() {
    BeanTester beanTester = new BeanTester();
    beanTester.testBean(PaymentValidationError.class);
  }

}
