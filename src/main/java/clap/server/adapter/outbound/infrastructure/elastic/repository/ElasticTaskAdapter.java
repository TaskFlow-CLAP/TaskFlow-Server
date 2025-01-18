package clap.server.adapter.outbound.infrastructure.elastic.repository;

import clap.server.adapter.outbound.infrastructure.elastic.document.ElasticTask;
import clap.server.application.port.outbound.ElasticTaskPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ElasticTaskAdapter implements ElasticTaskPort {
    private final TaskElasticRepository taskElasticRepository;

    @Override
    public void saveStatistics(List<ElasticTask> Statistics) {
        taskElasticRepository.saveAll(Statistics);
    }
}
