package ea.slartibartfast.demo.elasticsearch.configuration;

import ea.slartibartfast.demo.elasticsearch.configuration.properties.ElasticsearchProperties;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@RequiredArgsConstructor
@Configuration
@EnableElasticsearchRepositories(basePackages = "ea.slartibartfast.demo.elasticsearch.repository")
@ComponentScan(basePackages = { "ea.slartibartfast.demo.elasticsearch" })
public class ElasticsearchConfiguration {

    private final ElasticsearchProperties elasticsearchProperties;

    @Bean
    public RestHighLevelClient client() {
        ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                                                                     .connectedTo(elasticsearchProperties.getUrl())
                                                                     .build();

        return RestClients.create(clientConfiguration).rest();
    }

    @Bean
    public ElasticsearchOperations elasticsearchTemplate() {
        return new ElasticsearchRestTemplate(client());
    }
}
