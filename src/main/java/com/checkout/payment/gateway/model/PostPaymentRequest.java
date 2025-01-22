package com.checkout.payment.gateway.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;
import java.io.Serializable;

public class PostPaymentRequest implements Serializable {

  public PostPaymentRequest(Integer cardNumberLastFour, String cardNumber, Integer expiryMonth,
      Integer expiryYear, String currency, Integer amount, String cvv) {
    this.cardNumberLastFour = cardNumberLastFour;
    this.cardNumber = cardNumber;
    this.expiryMonth = expiryMonth;
    this.expiryYear = expiryYear;
    this.currency = currency;
    this.amount = amount;
    this.cvv = cvv;
  }

  public PostPaymentRequest() {
  }

  @JsonProperty("card_number_last_four")
  private Integer cardNumberLastFour;

  @NotNull(message = "card number is required")
  @Length(min = 14, max = 19, message = "card number must be 14-19 characters long")
  @Pattern(regexp = "\\d+", message = "card number must contain only numeric characters")
//  @JsonProperty(access = Access.WRITE_ONLY) not required as we do not persist these objects
  private String cardNumber;

  @NotNull(message = "expiry month is required")
  @Min(value = 1, message = "month must be >= 1")
  @Max(value = 12, message = "month must be <= 12")
  @JsonProperty("expiry_month")
  private Integer expiryMonth;

  @NotNull(message = "expiry year is required")
  @JsonProperty("expiry_year")
  private Integer expiryYear;

  @Length(min = 3, max = 3, message = "currency must be 3 characters")
  @Pattern(regexp = "GBP|USD|EUR", message = "currency must be GBP, USD, or EUR")
  private String currency;

  @NotNull(message = "amount is required")
  private Integer amount;

  @Pattern(regexp = "\\d+", message = "cvv must contain only numeric characters")
  @Length(min = 3, max = 4, message = "cvv must be 3-4 characters long")
  @NotNull(message = "cvv required")
  private String cvv;

  public String getCardNumber() {
    return cardNumber;
  }

  public void setCardNumber(String cardNumber) {
    this.cardNumber = cardNumber;
    this.cardNumberLastFour = extractLastFourDigits(cardNumber);
  }

  private Integer extractLastFourDigits(String cardNumber) {
    if (cardNumber == null || !cardNumber.matches("\\d+") || cardNumber.length() < 14 || cardNumber.length() > 19) {
      return null;
    }
    return Integer.valueOf(cardNumber.substring(cardNumber.length() - 4));
  }

  public Integer getCardNumberLastFour() {
    return cardNumberLastFour;
  }

  public void setCardNumberLastFour(Integer cardNumberLastFour) {
    this.cardNumberLastFour = cardNumberLastFour;
  }

  public Integer getExpiryMonth() {
    return expiryMonth;
  }

  public void setExpiryMonth(Integer expiryMonth) {
    this.expiryMonth = expiryMonth;
  }

  public Integer getExpiryYear() {
    return expiryYear;
  }

  public void setExpiryYear(Integer expiryYear) {
    this.expiryYear = expiryYear;
  }

  @JsonIgnore
  public String getFormattedExpiryDate() {
    if (expiryMonth != null && expiryYear != null) {
      return String.format("%02d/%d", expiryMonth, expiryYear);
    }
    return null;
  }


  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }

  public Integer getAmount() {
    return amount;
  }

  public void setAmount(Integer amount) {
    this.amount = amount;
  }

  public String getCvv() {
    return cvv;
  }

  public void setCvv(String cvv) {
    this.cvv = cvv;
  }



  @JsonProperty("expiry_date")
  public String getExpiryDate() {
    return String.format("%d/%d", expiryMonth, expiryYear);
  }

  @Override
  public String toString() {
    return "PostPaymentRequest{" +
        "cardNumberLastFour=" + cardNumberLastFour +
        ", expiryMonth=" + expiryMonth +
        ", expiryYear=" + expiryYear +
        ", currency='" + currency + '\'' +
        ", amount=" + amount +
        ", cvv=" + cvv +
        '}';
  }
}
