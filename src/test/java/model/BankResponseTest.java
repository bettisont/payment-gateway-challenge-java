package model;

import com.checkout.payment.gateway.model.BankResponse;
import org.junit.jupiter.api.Test;
import org.meanbean.test.BeanTester;

public class BankResponseTest {

  @Test
  void testJavaBeanProperties() {
    BeanTester beanTester = new BeanTester();
    beanTester.testBean(BankResponse.class);
  }

}
