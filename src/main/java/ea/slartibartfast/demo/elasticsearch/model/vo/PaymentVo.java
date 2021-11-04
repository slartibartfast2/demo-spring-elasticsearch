package ea.slartibartfast.demo.elasticsearch.model.vo;

import ea.slartibartfast.demo.elasticsearch.model.PaymentChannel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class PaymentVo {

    private Long documentNum;
    private BigDecimal amount;
    private String currency;
    private String customerFullName;
    private String emailAddress;
    private String merchantName;
    private PaymentChannel paymentChannel;
    private Date transactionDate;
    private String itemName;
}
