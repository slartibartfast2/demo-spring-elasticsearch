package ea.slartibartfast.demo.elasticsearch.controller;

import ea.slartibartfast.demo.elasticsearch.model.request.InvoiceRequest;
import ea.slartibartfast.demo.elasticsearch.model.request.PaymentRequest;
import ea.slartibartfast.demo.elasticsearch.model.request.PaymentSearchByPriceRequest;
import ea.slartibartfast.demo.elasticsearch.model.response.Response;
import ea.slartibartfast.demo.elasticsearch.model.vo.InvoiceVo;
import ea.slartibartfast.demo.elasticsearch.model.vo.PaymentVo;
import ea.slartibartfast.demo.elasticsearch.service.InvoiceService;
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
    private final InvoiceService invoiceService;
    private final PaymentServiceWithEsOperations paymentServiceWithEsOperations;

    @PostMapping
    public Response add(@RequestBody @Valid PaymentRequest request) {
        return paymentService.save(request);
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

    @PostMapping("/invoice")
    public Response createInvoice(@RequestBody @Valid InvoiceRequest request) {
        return invoiceService.save(request);
    }

    @GetMapping("/search/invoiced")
    public List<PaymentVo> retrieveInvoicedPayments() {
        return paymentServiceWithEsOperations.retrieveInvoicedPayments();
    }

    @GetMapping("/search/invoices/{paymentNum}")
    public List<InvoiceVo> retrieveInvoices(@PathVariable("paymentNum") Long paymentNum) {
        return paymentServiceWithEsOperations.retrievePaymentInvoices(paymentNum);
    }
}
