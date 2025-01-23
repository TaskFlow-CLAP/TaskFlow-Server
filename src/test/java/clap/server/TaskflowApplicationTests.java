package clap.server;

import com.redis.testcontainers.RedisContainer;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.assertj.core.api.Assertions.assertThat;

//@DataElasticsearchTest
@Testcontainers
@SpringBootTest
class TaskflowApplicationTests {

    @Container
//    @ServiceConnection
    public static ElasticsearchContainer ES_CONTAINER = new ElasticsearchContainer("docker.elastic.co/elasticsearch/elasticsearch:7.17.5");

    @Container
    @ServiceConnection
    public static RedisContainer REDIS_CONTAINER = new RedisContainer(DockerImageName.parse("redis:6.2.6"));

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        // Elasticsearch 설정
        registry.add("spring.elasticsearch.uris", ES_CONTAINER::getHttpHostAddress);
    }

    @Test
    void contextLoads() {
        assertThat(ES_CONTAINER.isRunning()).isTrue();
        assertThat(REDIS_CONTAINER.isRunning()).isTrue();
        System.out.println("Redis: " + REDIS_CONTAINER.getHost() + ":" + REDIS_CONTAINER.getMappedPort(6379));
        System.out.println("Elasticsearch URL: " + ES_CONTAINER.getHttpHostAddress());
    }
}
