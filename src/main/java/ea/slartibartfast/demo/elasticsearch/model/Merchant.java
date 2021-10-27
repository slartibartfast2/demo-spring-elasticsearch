package ea.slartibartfast.demo.elasticsearch.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Merchant {

    @Field(type = FieldType.Keyword, ignoreAbove = 50)
    private String merchantNum;

    @Field(type = FieldType.Text)
    private String merchantName;
}
