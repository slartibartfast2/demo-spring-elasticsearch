package ea.slartibartfast.demo.elasticsearch.model.response;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Response {

    private String status;
    private String errorCode;
    private String errorMessage;
}
