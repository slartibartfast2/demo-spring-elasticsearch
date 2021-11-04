package ea.slartibartfast.demo.elasticsearch.model.converter;

import ea.slartibartfast.demo.elasticsearch.model.PaymentDocument;
import ea.slartibartfast.demo.elasticsearch.model.vo.PaymentVo;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class PaymentVoConverter implements Function<PaymentDocument, PaymentVo> {

    @Override
    public PaymentVo apply(PaymentDocument payment) {
        return PaymentVo.builder()
                        .documentNum(payment.getDocumentNum())
                        .currency(payment.getCurrencyCode())
                        .customerFullName(payment.getCustomerFullName())
                        .paymentChannel(payment.getChannel())
                        .emailAddress(payment.getCustomer().getEmail())
                        .merchantName(payment.getMerchant().getMerchantName())
                        .amount(payment.getAmount())
                        .transactionDate(payment.getTransactionDate())
                        .itemName(payment.getItemName())
                        .build();
    }
}
