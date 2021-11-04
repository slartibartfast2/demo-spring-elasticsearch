package ea.slartibartfast.demo.elasticsearch.model.converter;

import ea.slartibartfast.demo.elasticsearch.model.PaymentDocument;
import ea.slartibartfast.demo.elasticsearch.model.vo.InvoiceVo;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class InvoiceVoConverter implements Function<PaymentDocument, InvoiceVo> {

    @Override
    public InvoiceVo apply(PaymentDocument paymentDocument) {
        return InvoiceVo.builder()
                .invoiceNum(paymentDocument.getDocumentNum())
                .amount(paymentDocument.getAmount())
                        .build();
    }
}
