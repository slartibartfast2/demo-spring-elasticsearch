package ea.slartibartfast.demo.elasticsearch.repository;

import ea.slartibartfast.demo.elasticsearch.model.Payment;
import ea.slartibartfast.demo.elasticsearch.model.PaymentChannel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.repository.query.Param;

public interface PaymentRepository extends ElasticsearchRepository<Payment, Long> {

    Page<Payment> findByChannel(PaymentChannel channel, Pageable pageable);

    Page<Payment> findByCustomerName(String customerName, Pageable pageable);

    @Query("{\"bool\": {\"must\": [{\"match\": {\"merchant.name\": \":merchantName\"}}]}}")
    Page<Payment> findByMerchantNameUsingCustomQuery(@Param("merchantName") String merchantName, Pageable pageable);
}
