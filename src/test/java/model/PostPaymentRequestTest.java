package model;
import com.checkout.payment.gateway.controller.BaseTest;
import com.checkout.payment.gateway.model.PostPaymentRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import jakarta.validation.Validator;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class PostPaymentRequestTest extends BaseTest {

  private Validator validator;

  @BeforeEach
  public void setUp() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  @Test
  public void testValidPostPaymentRequest() {

    Set<ConstraintViolation<PostPaymentRequest>> violations = validator.validate(
        validPaymentRequest);
    assertTrue(violations.isEmpty(), "Expected no validation errors");
  }

  @Test
  public void test_get_getFormattedExpiryDate() {
    assertEquals("04/2025", validPaymentRequest.getFormattedExpiryDate());
  }

  @Test
  public void test_get_getFormattedExpiryDate_null_year() {
    validPaymentRequest.setExpiryYear(null);
    assertNull(validPaymentRequest.getFormattedExpiryDate());
  }

  @Test
  public void test_get_getFormattedExpiryDate_null_month() {
    validPaymentRequest.setExpiryMonth(null);
    assertNull(validPaymentRequest.getFormattedExpiryDate());
  }

  @Test
  public void card_number_non_numerical() {
    validPaymentRequest.setCardNumber("ldwfvlwdkfjnvlksdn");

    Set<ConstraintViolation<PostPaymentRequest>> violations = validator.validate(
        validPaymentRequest);
    assertTrue(violations.iterator().next().getMessage().contains("card number must contain only numeric characters"));
    assertEquals(1, violations.size());
  }

  @Test
  void test_extract_last_four_card_number_is_null() {
    validPaymentRequest.setCardNumber(null);
    assertNull(validPaymentRequest.getCardNumberLastFour());
  }

  @Test
  void test_extract_last_four_card_number_is_alpha() {
    validPaymentRequest.setCardNumber("848484848484848ab");
    assertNull(validPaymentRequest.getCardNumberLastFour());
  }

  @Test
  void test_extract_last_four_card_number_too_short() {
    validPaymentRequest.setCardNumber("1234");
    assertNull(validPaymentRequest.getCardNumberLastFour());
  }

  @Test
  void test_extract_last_four_card_number_too_long() {
    validPaymentRequest.setCardNumber("123412341234123412341234");
    assertNull(validPaymentRequest.getCardNumberLastFour());
  }

  @Test
  public void card_number_wrong_length() {
    validPaymentRequest.setCardNumber("10000000000023423450000000000");

    Set<ConstraintViolation<PostPaymentRequest>> violations = validator.validate(
        validPaymentRequest);
    assertTrue(violations.iterator().next().getMessage().contains("card number must be 14-19 characters long"));
    assertEquals(1, violations.size());
  }

  @Test
  public void card_number_not_provided() {
    validPaymentRequest.setCardNumber(null);

    Set<ConstraintViolation<PostPaymentRequest>> violations = validator.validate(
        validPaymentRequest);
    assertTrue(violations.iterator().next().getMessage().contains("card number is required"));
    assertEquals(1, violations.size());
  }

  @Test
  public void expirymonth_not_provided() {
    validPaymentRequest.setExpiryMonth(null);

    Set<ConstraintViolation<PostPaymentRequest>> violations = validator.validate(
        validPaymentRequest);
    assertTrue(violations.iterator().next().getMessage().contains("expiry month is required"));
    assertEquals(1, violations.size());
  }

  @Test
  public void expiry_month_too_big() {
    validPaymentRequest.setExpiryMonth(100);

    Set<ConstraintViolation<PostPaymentRequest>> violations = validator.validate(
        validPaymentRequest);
    assertTrue(violations.iterator().next().getMessage().contains("month must be <= 12"));
    assertEquals(1, violations.size());
  }

  @Test
  public void expiry_month_too_small() {
    validPaymentRequest.setExpiryMonth(-5);

    Set<ConstraintViolation<PostPaymentRequest>> violations = validator.validate(
        validPaymentRequest);
    assertTrue(violations.iterator().next().getMessage().contains("month must be >= 1"));
    assertEquals(1, violations.size());
  }

  @Test
  public void expiry_year_not_provided() {
    validPaymentRequest.setExpiryYear(null);

    Set<ConstraintViolation<PostPaymentRequest>> violations = validator.validate(
        validPaymentRequest);
    assertTrue(violations.iterator().next().getMessage().contains("expiry year is required"));
    assertEquals(1, violations.size());
  }

  @Test
  public void inavlid_currency_code() {
    validPaymentRequest.setCurrency("JPY");

    Set<ConstraintViolation<PostPaymentRequest>> violations = validator.validate(
        validPaymentRequest);
    assertTrue(violations.iterator().next().getMessage().contains("currency must be GBP, USD, or EUR"));
    assertEquals(1, violations.size());
  }

  @Test
  public void currency_code_too_long() {
    validPaymentRequest.setCurrency("JPEY");

    Set<ConstraintViolation<PostPaymentRequest>> violations = validator.validate(
        validPaymentRequest);
    Set<String> errors = violations.stream().map(ConstraintViolation::getMessage)
        .collect(Collectors.toSet());
    assertEquals(2, violations.size());
    assertTrue(errors.contains("currency must be 3 characters"));
    assertTrue(errors.contains("currency must be GBP, USD, or EUR"));
  }

  @Test
  public void currency_code_too_short() {
    validPaymentRequest.setCurrency("LP");

    Set<ConstraintViolation<PostPaymentRequest>> violations = validator.validate(
        validPaymentRequest);
    Set<String> errors = violations.stream().map(ConstraintViolation::getMessage)
        .collect(Collectors.toSet());
    assertEquals(2, violations.size());
    assertTrue(errors.contains("currency must be 3 characters"));
    assertTrue(errors.contains("currency must be GBP, USD, or EUR"));
  }

  @Test
  public void amount_not_provided() {
    validPaymentRequest.setAmount(null);

    Set<ConstraintViolation<PostPaymentRequest>> violations = validator.validate(
        validPaymentRequest);
    assertTrue(violations.iterator().next().getMessage().contains("amount is required"));
    assertEquals(1, violations.size());
  }

  @Test
  public void cvv_not_provided() {
    validPaymentRequest.setCvv(null);

    Set<ConstraintViolation<PostPaymentRequest>> violations = validator.validate(
        validPaymentRequest);
    assertTrue(violations.iterator().next().getMessage().contains("cvv required"));
    assertEquals(1, violations.size());
  }

  @Test
  public void cvv_too_long() {
    validPaymentRequest.setCvv("17234");

    Set<ConstraintViolation<PostPaymentRequest>> violations = validator.validate(
        validPaymentRequest);
    assertTrue(violations.iterator().next().getMessage().contains("cvv must be 3-4 characters long"));
    assertEquals(1, violations.size());
  }

  @Test
  public void cvv_too_short() {
    validPaymentRequest.setCvv("14");

    Set<ConstraintViolation<PostPaymentRequest>> violations = validator.validate(
        validPaymentRequest);
    assertTrue(violations.iterator().next().getMessage().contains("cvv must be 3-4 characters long"));
    assertEquals(1, violations.size());
  }

  @Test
  public void cvv_alphanumeric() {
    validPaymentRequest.setCvv("a3b");

    Set<ConstraintViolation<PostPaymentRequest>> violations = validator.validate(
        validPaymentRequest);
    assertTrue(violations.iterator().next().getMessage().contains("cvv must contain only numeric characters"));
    assertEquals(1, violations.size());
  }




}
