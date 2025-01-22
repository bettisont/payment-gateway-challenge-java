package model;

import com.checkout.payment.gateway.model.GetPaymentResponse;
import org.junit.jupiter.api.Test;
import org.meanbean.test.BeanTester;
import testUtils.UUIDFactory;
import java.util.UUID;

public class GetPaymentResponseTest {

  @Test
  void testJavaBeanProperties() {
    BeanTester beanTester = new BeanTester();
    beanTester.getFactoryCollection().addFactory(UUID.class,
        new UUIDFactory());
    beanTester.testBean(GetPaymentResponse.class);
  }

}
