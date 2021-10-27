package ea.slartibartfast.demo.elasticsearch.controller;

import ea.slartibartfast.demo.elasticsearch.model.request.PaymentRequest;
import ea.slartibartfast.demo.elasticsearch.model.request.PaymentSearchByPriceRequest;
import ea.slartibartfast.demo.elasticsearch.model.response.Response;
import ea.slartibartfast.demo.elasticsearch.model.vo.PaymentVo;
import ea.slartibartfast.demo.elasticsearch.service.PaymentService;
import ea.slartibartfast.demo.elasticsearch.service.PaymentServiceWithEsOperations;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {

    private final PaymentService paymentService;
    private final PaymentServiceWithEsOperations paymentServiceWithEsOperations;

    @PostMapping
    public Response add(@RequestBody @Valid PaymentRequest employee) {
        return paymentService.save(employee);
    }

    @GetMapping("/customer/{customerName}")
    public List<PaymentVo> retrievePayments(@PathVariable("customerName") String customerName) {
        return paymentService.retrievePaymentsByCustomerName(customerName);
    }

    @GetMapping("/merchant/{merchantName}")
    public List<PaymentVo> findByMerchantName(@PathVariable("merchantName") String merchantName) {
        return paymentService.retrievePaymentsByMerchantName(merchantName);
    }

    @GetMapping("/search/item/{itemName}")
    public List<PaymentVo> searchByItemName(@PathVariable("itemName") String itemName) {
        return paymentServiceWithEsOperations.retrievePaymentsByItemName(itemName);
    }

    @GetMapping("/search/merchant/{merchantName}")
    public List<PaymentVo> searchByMerchantName(@PathVariable("merchantName") String merchantName, @RequestParam("page") Integer page, @RequestParam("size") Integer size) {
        return paymentServiceWithEsOperations.retrievePaymentsByMerchantName(merchantName, page, size);
    }

    @GetMapping("/search/price")
    public List<PaymentVo> searchByPrice(@RequestBody @Valid PaymentSearchByPriceRequest paymentSearchByPriceRequest) {
        return paymentServiceWithEsOperations.retrievePaymentsByPrice(paymentSearchByPriceRequest);
    }
}
