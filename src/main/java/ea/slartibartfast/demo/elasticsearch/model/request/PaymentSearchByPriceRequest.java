package ea.slartibartfast.demo.elasticsearch.model.request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class PaymentSearchByPriceRequest {

    private BigDecimal upperPrice;
    private BigDecimal lowerPrice;
}
