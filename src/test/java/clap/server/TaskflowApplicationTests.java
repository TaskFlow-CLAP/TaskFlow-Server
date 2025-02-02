package clap.server;

import clap.server.adapter.outbound.persistense.repository.member.MemberRepository;
import com.redis.testcontainers.RedisContainer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest
@TestPropertySource(properties = "spring.flyway.enabled=false")
class TaskflowApplicationTests {

    @Autowired
    private MemberRepository memberRepository;

    @Container
    public static ElasticsearchContainer ES_CONTAINER = new ElasticsearchContainer("docker.elastic.co/elasticsearch/elasticsearch:7.17.5")
            .withReuse(true);

    @DynamicPropertySource
    static void elasticProperties(DynamicPropertyRegistry registry) {
        // Elasticsearch 설정
        registry.add("spring.elasticsearch.uris", ES_CONTAINER::getHttpHostAddress);
    }

    @Container
    public static RedisContainer REDIS_CONTAINER = new RedisContainer(DockerImageName.parse("redis:6.2.6"))
            .withReuse(true);

    @DynamicPropertySource
    static void redisProperties(DynamicPropertyRegistry registry) {
        // redis 설정
        registry.add("spring.data.redis.host", REDIS_CONTAINER::getHost);
        registry.add("spring.data.redis.port", () -> REDIS_CONTAINER.getMappedPort(6379));
    }

    @Test
    void contextLoads() {
        assertThat(memberRepository.findAll()).isEmpty();
        assertThat(ES_CONTAINER.isRunning()).isTrue();
        assertThat(REDIS_CONTAINER.isRunning()).isTrue();
        System.out.println("Redis: " + REDIS_CONTAINER.getHost() + ":" + REDIS_CONTAINER.getMappedPort(6379));
        System.out.println("Elasticsearch URL: " + ES_CONTAINER.getHttpHostAddress());
    }
}
