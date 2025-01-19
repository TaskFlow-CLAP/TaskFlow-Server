package clap.server.adapter.outbound.infrastructure.elastic;

import clap.server.adapter.outbound.infrastructure.elastic.entity.ElasticTask;
import clap.server.adapter.outbound.infrastructure.elastic.repository.TaskElasticRepository;
import clap.server.application.port.outbound.task.ElasticTaskPort;
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
