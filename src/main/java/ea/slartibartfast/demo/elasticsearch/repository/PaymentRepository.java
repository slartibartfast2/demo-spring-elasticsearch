package ea.slartibartfast.demo.elasticsearch.repository;

import ea.slartibartfast.demo.elasticsearch.model.PaymentDocument;
import ea.slartibartfast.demo.elasticsearch.model.PaymentChannel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PaymentRepository extends ElasticsearchRepository<PaymentDocument, Long> {

    Optional<PaymentDocument> findByDocumentNum(Long documentNum);

    Page<PaymentDocument> findByChannel(PaymentChannel channel, Pageable pageable);

    Page<PaymentDocument> findByCustomerName(String customerName, Pageable pageable);

    @Query("{\"bool\": {\"must\": [{\"match\": {\"merchant.name\": \":merchantName\"}}]}}")
    Page<PaymentDocument> findByMerchantNameUsingCustomQuery(@Param("merchantName") String merchantName, Pageable pageable);
}
