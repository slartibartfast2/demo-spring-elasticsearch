package ea.slartibartfast.demo.elasticsearch.model.converter;

import ea.slartibartfast.demo.elasticsearch.model.Customer;
import ea.slartibartfast.demo.elasticsearch.model.Merchant;
import ea.slartibartfast.demo.elasticsearch.model.Payment;
import ea.slartibartfast.demo.elasticsearch.model.request.PaymentRequest;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.function.Function;

@Component
public class PaymentRequestConverter implements Function<PaymentRequest, Payment> {

    @Override
    public Payment apply(PaymentRequest paymentRequest) {
        return Payment.builder()
                      .price(paymentRequest.getPrice())
                      .currencyCode(paymentRequest.getCurrency())
                      .customer(prepareCustomer(paymentRequest))
                      .merchant(prepareMerchant(paymentRequest))
                      .channel(paymentRequest.getPaymentChannel())
                      .transactionDate(now())
                      .build();
    }

    private Customer prepareCustomer(PaymentRequest paymentRequest) {
        return Customer.builder()
                       .email(paymentRequest.getEmailAddress())
                       .gsmNumber(paymentRequest.getGsmNumber())
                       .name(paymentRequest.getCustomerName())
                       .surname(paymentRequest.getCustomerSurname())
                       .build();
    }

    private Merchant prepareMerchant(PaymentRequest paymentRequest) {
        return Merchant.builder()
                       .merchantName(paymentRequest.getMerchantName())
                       .merchantNum(paymentRequest.getMerchantNum())
                       .build();
    }

    private Date now() {
        return Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
    }
}
