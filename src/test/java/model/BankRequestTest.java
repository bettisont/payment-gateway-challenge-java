package model;
import com.checkout.payment.gateway.model.BankRequest;
import org.junit.jupiter.api.Test;
import org.meanbean.test.BeanTester;

public class BankRequestTest {

  @Test
  void testJavaBeanProperties() {
    BeanTester beanTester = new BeanTester();
    beanTester.testBean(BankRequest.class);
  }

}
