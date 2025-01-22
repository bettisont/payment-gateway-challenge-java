package model;

import com.checkout.payment.gateway.model.ErrorResponse;
import org.junit.jupiter.api.Test;
import org.meanbean.test.BeanTester;

public class ErrorResponseTest {

  @Test
  void testJavaBeanProperties() {
    BeanTester beanTester = new BeanTester();
    beanTester.testBean(ErrorResponse.class);
  }

}
