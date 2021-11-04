package ea.slartibartfast.demo.elasticsearch;

import ea.slartibartfast.demo.elasticsearch.model.*;
import ea.slartibartfast.demo.elasticsearch.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.join.JoinField;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Slf4j
@SpringBootApplication
public class DemoSpringElasticsearchApplication {

    private final ElasticsearchOperations elasticsearchTemplate;
    private final PaymentRepository paymentRepository;

    public static void main(String[] args) {
        SpringApplication.run(DemoSpringElasticsearchApplication.class, args);
    }

    @PreDestroy
    public void deleteIndex() {
        elasticsearchTemplate.indexOps(PaymentDocument.class).delete();
    }

    @PostConstruct
    public void buildIndex() {
        elasticsearchTemplate.indexOps(PaymentDocument.class).refresh();

        paymentRepository.deleteAll();
        paymentRepository.saveAll(prepareDataset());
    }

    private Collection<PaymentDocument> prepareDataset() {
        List<PaymentDocument> paymentList = new ArrayList<>();

        try (Stream<String> csvRows = Files.lines(Paths.get(ClassLoader.getSystemResource("payment-data.csv").toURI()), StandardCharsets.UTF_8)) {
            paymentList = csvRows.skip(1)
                                 .map(this::csvRowToPaymentMapper)
                                 .filter(Optional::isPresent)
                                 .map(Optional::get)
                                 .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("File read error", e);
        }

        return paymentList;
    }

    private Optional<PaymentDocument> csvRowToPaymentMapper(final String line) {
        try (Scanner rowScanner = new Scanner(line)) {
            rowScanner.useDelimiter(Constants.CSV_DELIMITER);

            Long paymentId = rowScanner.nextLong();
            BigDecimal price = rowScanner.nextBigDecimal();
            String currencyCode = rowScanner.next();
            String paymentChannel = rowScanner.next();
            String customerName = rowScanner.next();
            String customerSurname = rowScanner.next();
            String customerEmail = rowScanner.next();
            String customerGsmNumber = rowScanner.next();
            String merchantNum = rowScanner.next();
            String merchantName = rowScanner.next();
            String transactionDate = rowScanner.next();
            String itemName = rowScanner.next();

            return Optional.of(
                    PaymentDocument.builder()
                                   .documentNum(paymentId)
                                   .amount(price)
                                   .currencyCode(currencyCode)
                                   .customer(prepareCustomer(customerName, customerSurname, customerGsmNumber, customerEmail))
                                   .merchant(prepareMerchant(merchantNum, merchantName))
                                   .channel(PaymentChannel.valueOf(paymentChannel))
                                   .transactionDate(prepareDate(transactionDate))
                                   .itemName(itemName)
                                   .invoiceRelation(new JoinField<>("payment"))
                                   .build());
        }
    }

    private Customer prepareCustomer(String customerName, String customerSurname, String gsmNumber, String email) {
        return Customer.builder()
                       .email(email)
                       .gsmNumber(gsmNumber)
                       .name(customerName)
                       .surname(customerSurname)
                       .build();
    }

    private Merchant prepareMerchant(String merchantNum, String merchantName) {
        return Merchant.builder().merchantNum(merchantNum).merchantName(merchantName).build();
    }

    private Date prepareDate(String transactionDate) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
            return formatter.parse(transactionDate);
        } catch (ParseException e) {
            log.error("date parsing error", e);
        }

        return null;
    }

}
