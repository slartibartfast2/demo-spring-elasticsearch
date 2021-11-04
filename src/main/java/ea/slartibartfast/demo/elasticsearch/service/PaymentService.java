package ea.slartibartfast.demo.elasticsearch.service;

import ea.slartibartfast.demo.elasticsearch.model.PaymentDocument;
import ea.slartibartfast.demo.elasticsearch.model.converter.PaymentRequestConverter;
import ea.slartibartfast.demo.elasticsearch.model.converter.PaymentVoConverter;
import ea.slartibartfast.demo.elasticsearch.model.request.PaymentRequest;
import ea.slartibartfast.demo.elasticsearch.model.response.Response;
import ea.slartibartfast.demo.elasticsearch.model.vo.PaymentVo;
import ea.slartibartfast.demo.elasticsearch.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentRequestConverter paymentRequestConverter;
    private final PaymentVoConverter paymentVoConverter;

    public Response save(PaymentRequest paymentRequest) {
        PaymentDocument document = paymentRequestConverter.apply(paymentRequest);
        paymentRepository.save(document);
        return Response.builder().status("SUCCESS").build();
    }

    public List<PaymentVo> retrievePaymentsByCustomerName(String customerName) {
        final Page<PaymentDocument> paymentsPage = paymentRepository.findByCustomerName(customerName, PageRequest.of(0, 20));
        return paymentsPage.getContent().stream().map(paymentVoConverter).collect(Collectors.toList());
    }

    public List<PaymentVo> retrievePaymentsByMerchantName(String merchantName) {
        final Page<PaymentDocument> paymentsPage = paymentRepository.findByMerchantNameUsingCustomQuery(merchantName, PageRequest.of(0, 20));
        return paymentsPage.getContent().stream().map(paymentVoConverter).collect(Collectors.toList());
    }
}
