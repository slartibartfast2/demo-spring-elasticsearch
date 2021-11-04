package ea.slartibartfast.demo.elasticsearch.service;

import ea.slartibartfast.demo.elasticsearch.exception.PaymentNotFoundException;
import ea.slartibartfast.demo.elasticsearch.model.PaymentDocument;
import ea.slartibartfast.demo.elasticsearch.model.converter.InvoiceRequestConverter;
import ea.slartibartfast.demo.elasticsearch.model.request.InvoiceRequest;
import ea.slartibartfast.demo.elasticsearch.model.response.Response;
import ea.slartibartfast.demo.elasticsearch.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class InvoiceService {

    private final PaymentRepository paymentRepository;
    private final InvoiceRequestConverter invoiceRequestConverter;

    public Response save(InvoiceRequest request) {
        String paymentDocumentNum = paymentRepository.findByDocumentNum(request.getPaymentNumber())
                                                    .orElseThrow(PaymentNotFoundException::new)
                                                    .getId();

        PaymentDocument document = invoiceRequestConverter.apply(request, paymentDocumentNum);
        paymentRepository.save(document);
        return Response.builder().status("SUCCESS").build();
    }
}
