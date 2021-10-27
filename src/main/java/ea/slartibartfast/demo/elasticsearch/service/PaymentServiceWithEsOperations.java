package ea.slartibartfast.demo.elasticsearch.service;

import ea.slartibartfast.demo.elasticsearch.model.Payment;
import ea.slartibartfast.demo.elasticsearch.model.converter.PaymentVoConverter;
import ea.slartibartfast.demo.elasticsearch.model.request.PaymentSearchByPriceRequest;
import ea.slartibartfast.demo.elasticsearch.model.vo.PaymentVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

import static org.elasticsearch.index.query.QueryBuilders.matchQuery;

@Slf4j
@RequiredArgsConstructor
@Service
public class PaymentServiceWithEsOperations {

    private static final String PAYMENT_INDEX = "idx-payments-*";

    private final ElasticsearchOperations elasticsearchOperations;
    private final PaymentVoConverter paymentVoConverter;

    public List<PaymentVo> retrievePaymentsByItemName(final String itemName) {
        final NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(matchQuery("itemName", itemName).minimumShouldMatch("80%"))
                .build();

        SearchHits<Payment> paymentHits = elasticsearchOperations.search(searchQuery, Payment.class, IndexCoordinates.of(PAYMENT_INDEX));
        return convertSearchHitsToVo(paymentHits);
    }

    public List<PaymentVo> retrievePaymentsByMerchantName(final String merchantName, final Integer page, final Integer size) {
        final NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(matchQuery("merchant.merchantName", merchantName).operator(Operator.AND).fuzziness(Fuzziness.ONE).prefixLength(3))
                .withPageable(PageRequest.of(page-1, size))
                .build();

        SearchHits<Payment> paymentHits = elasticsearchOperations.search(searchQuery, Payment.class, IndexCoordinates.of(PAYMENT_INDEX));
        return convertSearchHitsToVo(paymentHits);
    }

    public List<PaymentVo> retrievePaymentsByPrice(final PaymentSearchByPriceRequest request) {
        Criteria criteria = new Criteria("price").greaterThan(request.getLowerPrice()).lessThan(request.getUpperPrice());
        Query searchQuery = new CriteriaQuery(criteria);
        searchQuery.addSort(Sort.by(Sort.Order.desc("price")));
        SearchHits<Payment> paymentHits = elasticsearchOperations.search(searchQuery, Payment.class, IndexCoordinates.of(PAYMENT_INDEX));
        return convertSearchHitsToVo(paymentHits);
    }

    private List<PaymentVo> convertSearchHitsToVo(SearchHits<Payment> paymentHits) {
        log.info("paymentHits {} {}", paymentHits.getSearchHits().size(), paymentHits.getSearchHits());

        List<SearchHit<Payment>> searchHits = paymentHits.getSearchHits();
        return searchHits.stream().map(paymentSearchHit -> paymentVoConverter.apply(paymentSearchHit.getContent())).collect(Collectors.toList());
    }
}
