package ea.slartibartfast.demo.elasticsearch.model;

import lombok.*;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

import javax.validation.constraints.Email;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Customer {

    @Field(type = FieldType.Text, analyzer = "custom_analyzer")
    private String name;

    @Field(type = FieldType.Text, analyzer = "custom_analyzer")
    private String surname;

    @Field(type = FieldType.Text)
    private String gsmNumber;

    @Field(type = FieldType.Text) @Email
    private String email;
}
