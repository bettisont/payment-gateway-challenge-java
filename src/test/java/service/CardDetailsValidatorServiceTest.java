package service;

import com.checkout.payment.gateway.controller.BaseTest;
import com.checkout.payment.gateway.exception.InvalidPaymentDetailsException;
import com.checkout.payment.gateway.service.CardDetailsValidatorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.time.YearMonth;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = CardDetailsValidatorService.class)
public class CardDetailsValidatorServiceTest extends BaseTest {

  @Autowired
  CardDetailsValidatorService service;

  @Test
  void test_date_is_in_past_throws_exception() {
    validPaymentRequest.setExpiryYear(2022);
    validPaymentRequest.setExpiryMonth(10);
    assertThrows(InvalidPaymentDetailsException.class, () -> service.validate(validPaymentRequest));
  }

  @Test
  void test_same_year_month_throws_exception() {
    validPaymentRequest.setExpiryMonth(YearMonth.now().getMonthValue());
    validPaymentRequest.setExpiryYear(YearMonth.now().getYear());

    InvalidPaymentDetailsException invalidPaymentDetailsException = assertThrows(
        InvalidPaymentDetailsException.class, () -> service.validate(validPaymentRequest));

    assertEquals("expiry date must be in the future.", invalidPaymentDetailsException.getMessage());
  }

  @Test
  void test_future_date_ok() {
    validPaymentRequest.setExpiryMonth(YearMonth.now().plusMonths(1).getMonthValue());
    validPaymentRequest.setExpiryYear(YearMonth.now().getYear());

    service.validate(validPaymentRequest);
  }
}
