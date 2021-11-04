package ea.slartibartfast.demo.elasticsearch.model.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Setter
@Getter
public class InvoiceRequest {

    @NotNull
    private Long invoiceNumber;

    @NotNull
    private Long paymentNumber;

    @NotNull @Positive
    private BigDecimal amount;
}
