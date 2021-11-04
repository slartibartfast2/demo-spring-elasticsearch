package ea.slartibartfast.demo.elasticsearch.model.converter;

import ea.slartibartfast.demo.elasticsearch.model.PaymentDocument;
import ea.slartibartfast.demo.elasticsearch.model.request.InvoiceRequest;
import org.springframework.data.elasticsearch.core.join.JoinField;
import org.springframework.stereotype.Component;

import java.util.function.BiFunction;

@Component
public class InvoiceRequestConverter implements BiFunction<InvoiceRequest, String, PaymentDocument> {

    @Override
    public PaymentDocument apply(InvoiceRequest invoiceRequest, String paymentDocumentId) {
        return PaymentDocument.builder()
                              .documentNum(invoiceRequest.getInvoiceNumber())
                              .amount(invoiceRequest.getAmount())
                              .invoiceRelation((new JoinField<>("invoice", paymentDocumentId)))
                              .build();
    }
}
