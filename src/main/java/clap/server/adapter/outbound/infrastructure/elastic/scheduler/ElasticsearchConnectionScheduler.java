package clap.server.adapter.outbound.infrastructure.elastic.scheduler;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ElasticsearchConnectionScheduler {
    private final ElasticsearchClient client;

    @Scheduled(fixedRate = 30000)
    public void keepConnectionAlive() {
        try {
            if (!client.ping().value()) {
                log.error("Elasticsearch 연결 실패");
            }
        } catch (Exception e) {
            log.error("Elasticsearch 연결 중 예외 발생: {}", e.getMessage());
        }
    }
}
