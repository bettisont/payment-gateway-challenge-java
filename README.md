# Instructions for candidates

This is the Java version of the Payment Gateway challenge. If you haven't already read this [README.md](https://github.com/cko-recruitment/) on the details of this exercise, please do so now.

## Requirements
- JDK 17
- Docker

## Template structure

src/ - A skeleton SpringBoot Application

test/ - Some simple JUnit tests

imposters/ - contains the bank simulator configuration. Don't change this

.editorconfig - don't change this. It ensures a consistent set of rules for submissions when reformatting code

docker-compose.yml - configures the bank simulator


## API Documentation
For documentation openAPI is included, and it can be found under the following url: **http://localhost:8090/swagger-ui/index.html**

**Feel free to change the structure of the solution, use a different library etc.**

# Tim Bettison Documentation 

## Design Decisions

### Seperation of Concerns
- PaymentGateWayService - central class for processing the payments
- PaymentMapper - logic converting between certain objects 
- BankService - logic for communicating with the bank 
- PaymentsRepository - handles persistence 

### Input Validation
- I decided to use @Valid in conjunction with BindingResult to do my payment validation - reducing boilerplate validation code 
- I only used custom test code where required, in the CardDetailsValidatorService (testing the date was in the future)
- Validating early means downstream components do not need to worry about this 

### Testing
- Integration tests: tested end to end payment scenarios in PaymentGatewayControllerTest
- Unit tests: mocked behaviour of dependencies for PaymentGatewayServiceTest, input validation tests in PostPaymentRequestTest
- meanbean: used for beanTesting the getters and setters of Java beans

### Security
- card numbers are not persisted

### Error handling 
- custom exceptions - BankCommunicationException for bank errors, CardPaymentProcessingException. Allows for easy debugging in case of errors

## Assumptions
1. In-memory storage is sufficient 
2. "Rejected" is sufficient feedback for clients when providing invalid card details (but details are logged)
4. performance and scalabiltiy is not critical at this stage - the system was built with simplicity in mind
5. The bank simulator is reliable and always reachable when valid details are provided (i.e. no retry mechanism built)
