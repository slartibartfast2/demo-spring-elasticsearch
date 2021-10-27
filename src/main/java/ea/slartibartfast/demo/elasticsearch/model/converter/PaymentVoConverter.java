package ea.slartibartfast.demo.elasticsearch.model.converter;

import ea.slartibartfast.demo.elasticsearch.model.Payment;
import ea.slartibartfast.demo.elasticsearch.model.vo.PaymentVo;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class PaymentVoConverter implements Function<Payment, PaymentVo> {

    @Override
    public PaymentVo apply(Payment payment) {
        return PaymentVo.builder()
                        .currency(payment.getCurrencyCode())
                        .customerFullName(payment.getCustomerFullName())
                        .paymentChannel(payment.getChannel())
                        .emailAddress(payment.getCustomer().getEmail())
                        .merchantName(payment.getMerchant().getMerchantName())
                        .price(payment.getPrice())
                        .transactionDate(payment.getTransactionDate())
                        .itemName(payment.getItemName())
                        .build();
    }
}
