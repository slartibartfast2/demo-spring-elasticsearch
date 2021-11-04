package ea.slartibartfast.demo.elasticsearch.model.converter;

import ea.slartibartfast.demo.elasticsearch.model.Customer;
import ea.slartibartfast.demo.elasticsearch.model.Merchant;
import ea.slartibartfast.demo.elasticsearch.model.PaymentDocument;
import ea.slartibartfast.demo.elasticsearch.model.request.PaymentRequest;
import org.springframework.data.elasticsearch.core.join.JoinField;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.function.Function;

@Component
public class PaymentRequestConverter implements Function<PaymentRequest, PaymentDocument> {

    @Override
    public PaymentDocument apply(PaymentRequest paymentRequest) {
        return PaymentDocument.builder()
                              .amount(paymentRequest.getPrice())
                              .currencyCode(paymentRequest.getCurrency())
                              .customer(prepareCustomer(paymentRequest))
                              .merchant(prepareMerchant(paymentRequest))
                              .channel(paymentRequest.getPaymentChannel())
                              .transactionDate(now())
                .invoiceRelation(new JoinField<>("payment"))
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
