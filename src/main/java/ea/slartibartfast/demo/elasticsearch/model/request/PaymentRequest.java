package ea.slartibartfast.demo.elasticsearch.model.request;

import ea.slartibartfast.demo.elasticsearch.model.PaymentChannel;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;
import java.math.BigDecimal;

@Setter
@Getter
public class PaymentRequest {

    @NotNull @Positive
    private BigDecimal price;

    @NotBlank
    private String currency;

    @NotBlank
    private String customerName;

    @NotBlank
    private String customerSurname;

    @NotBlank @Email
    private String emailAddress;

    @NotBlank
    private String gsmNumber;

    @NotBlank
    private String merchantNum;

    @NotBlank
    private String merchantName;

    @NotBlank
    private PaymentChannel paymentChannel;
}
