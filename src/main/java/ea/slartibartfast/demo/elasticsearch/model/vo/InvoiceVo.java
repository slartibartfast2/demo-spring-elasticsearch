package ea.slartibartfast.demo.elasticsearch.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class InvoiceVo {

    private Long invoiceNum;
    private BigDecimal amount;
}
