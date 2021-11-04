package ea.slartibartfast.demo.elasticsearch.service;

import ea.slartibartfast.demo.elasticsearch.model.PaymentDocument;
import ea.slartibartfast.demo.elasticsearch.model.converter.InvoiceVoConverter;
import ea.slartibartfast.demo.elasticsearch.model.converter.PaymentVoConverter;
import ea.slartibartfast.demo.elasticsearch.model.request.PaymentSearchByPriceRequest;
import ea.slartibartfast.demo.elasticsearch.model.vo.InvoiceVo;
import ea.slartibartfast.demo.elasticsearch.model.vo.PaymentVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.Operator;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static org.elasticsearch.index.query.QueryBuilders.*;
import static org.elasticsearch.join.query.JoinQueryBuilders.hasChildQuery;
import static org.elasticsearch.join.query.JoinQueryBuilders.parentId;

@Slf4j
@RequiredArgsConstructor
@Service
public class PaymentServiceWithEsOperations {

    private static final String PAYMENT_INDEX = "idx-payments-*";

    private final ElasticsearchOperations operations;
    private final PaymentVoConverter paymentVoConverter;
    private final InvoiceVoConverter invoiceVoConverter;

    public List<PaymentVo> retrievePaymentsByItemName(final String itemName) {
        final NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(matchQuery("itemName", itemName).minimumShouldMatch("80%"))
                .build();

        SearchHits<PaymentDocument> paymentHits = operations.search(searchQuery, PaymentDocument.class, IndexCoordinates.of(PAYMENT_INDEX));
        return convertPaymentSearchHitsToVo(paymentHits);
    }

    public List<PaymentVo> retrievePaymentsByMerchantName(final String merchantName, final Integer page, final Integer size) {
        final NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(matchQuery("merchant.merchantName", merchantName).operator(Operator.AND).fuzziness(Fuzziness.ONE).prefixLength(3))
                .withPageable(PageRequest.of(page-1, size))
                .build();

        SearchHits<PaymentDocument> paymentHits = operations.search(searchQuery, PaymentDocument.class, IndexCoordinates.of(PAYMENT_INDEX));
        return convertPaymentSearchHitsToVo(paymentHits);
    }

    public List<PaymentVo> retrievePaymentsByPrice(final PaymentSearchByPriceRequest request) {
        Criteria criteria = new Criteria("price").greaterThan(request.getLowerPrice()).lessThan(request.getUpperPrice());
        Query searchQuery = new CriteriaQuery(criteria);
        searchQuery.addSort(Sort.by(Sort.Order.desc("price")));
        SearchHits<PaymentDocument> paymentHits = operations.search(searchQuery, PaymentDocument.class, IndexCoordinates.of(PAYMENT_INDEX));
        return convertPaymentSearchHitsToVo(paymentHits);
    }

    public List<PaymentVo> retrieveInvoicedPayments() {
        NativeSearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(hasChildQuery("invoice", matchAllQuery(), ScoreMode.None))
                .build();

        SearchHits<PaymentDocument> paymentHits = operations.search(query, PaymentDocument.class);
        return convertPaymentSearchHitsToVo(paymentHits);
    }

    private PaymentDocument retrievePayment(final Long paymentNum) {
        NativeSearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(termQuery("documentNum", paymentNum))
                .build();

        SearchHit<PaymentDocument> paymentHit = operations.searchOne(query, PaymentDocument.class);
        return paymentHit.getContent();
    }

    public List<InvoiceVo> retrievePaymentInvoices(final Long paymentNum) {
        String paymentDocumentId = retrievePayment(paymentNum).getId();

        NativeSearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(parentId("invoice", paymentDocumentId))
                .build();

        SearchHits<PaymentDocument> paymentHits = operations.search(query, PaymentDocument.class);
        return convertInvoiceSearchHitsToVo(paymentHits);
    }

    private List<InvoiceVo> convertInvoiceSearchHitsToVo(SearchHits<PaymentDocument> paymentHits) {
        log.info("paymentHits {} {}", paymentHits.getSearchHits().size(), paymentHits.getSearchHits());

        List<SearchHit<PaymentDocument>> searchHits = paymentHits.getSearchHits();
        return searchHits.stream().map(paymentSearchHit -> invoiceVoConverter.apply(paymentSearchHit.getContent())).collect(Collectors.toList());
    }

    private List<PaymentVo> convertPaymentSearchHitsToVo(SearchHits<PaymentDocument> paymentHits) {
        log.info("paymentHits {} {}", paymentHits.getSearchHits().size(), paymentHits.getSearchHits());

        List<SearchHit<PaymentDocument>> searchHits = paymentHits.getSearchHits();
        return searchHits.stream().map(paymentSearchHit -> paymentVoConverter.apply(paymentSearchHit.getContent())).collect(Collectors.toList());
    }
}
