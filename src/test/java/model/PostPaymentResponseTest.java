package model;

import com.checkout.payment.gateway.model.PostPaymentResponse;
import org.junit.jupiter.api.Test;
import org.meanbean.test.BeanTester;
import testUtils.UUIDFactory;
import java.util.UUID;

public class PostPaymentResponseTest {

  @Test
  void testJavaBeanProperties() {
    BeanTester beanTester = new BeanTester();
    beanTester.getFactoryCollection().addFactory(UUID.class,
        new UUIDFactory());
    beanTester.testBean(PostPaymentResponse.class);
  }

}
