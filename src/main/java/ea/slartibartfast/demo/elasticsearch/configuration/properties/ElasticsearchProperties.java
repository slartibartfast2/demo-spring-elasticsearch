package ea.slartibartfast.demo.elasticsearch.configuration.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "elasticsearch")
@Setter
@Getter
@Component
public class ElasticsearchProperties {

    private String url;
}
