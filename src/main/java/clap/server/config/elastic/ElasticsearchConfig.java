package clap.server.config.elastic;

import clap.server.adapter.outbound.infrastructure.elastic.repository.TaskElasticRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories(basePackageClasses = {TaskElasticRepository.class})
public class ElasticsearchConfig extends ElasticsearchConfiguration {

    @Override
    public ClientConfiguration clientConfiguration() {

        return ClientConfiguration.builder()
                .connectedTo("127.0.0.1:9200")
                .withConnectTimeout(30000)
                .withSocketTimeout(30000)
                .build();
    }

}
