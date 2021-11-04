package ea.slartibartfast.demo.elasticsearch.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.elasticsearch.annotations.*;
import org.springframework.data.elasticsearch.core.join.JoinField;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setting(settingPath = "/elastic-settings.json")
@TypeAlias("payment-doc")
@Document(indexName = "idx-payments-#{T(java.time.LocalDate).now().toString()}")
public class PaymentDocument {

    @Id
    private String id;

    @Field(type = FieldType.Keyword) @NotBlank
    private Long documentNum;

    @Field(type= FieldType.Date, format = {DateFormat.date_time, DateFormat.date_hour_minute_second}) @NotBlank
    private Date transactionDate;

    @Field(type = FieldType.Float) @Positive
    private BigDecimal amount;

    @Field(type = FieldType.Keyword, ignoreAbove = 15) @NotBlank
    private String currencyCode;

    @Field(type = FieldType.Nested, includeInParent = true)
    private Customer customer;

    @Field(type = FieldType.Nested, includeInParent = true)
    private Merchant merchant;

    @Field(type = FieldType.Keyword, ignoreAbove = 15) @NotBlank
    private PaymentChannel channel;

    @Field(type = FieldType.Text) @NotBlank
    private String itemName;

    @JoinTypeRelations(
            relations = @JoinTypeRelation(parent = "payment", children = "invoice")
    )
    private JoinField<String> invoiceRelation;

    public String getCustomerFullName() {
        return Optional.ofNullable(customer).map(c -> String.join(" ", c.getName(), c.getSurname())).orElse(null);
    }
}
