package clap.server.adapter.outbound.infrastructure.elastic;

import clap.server.adapter.outbound.infrastructure.elastic.entity.ElasticTask;
import clap.server.adapter.outbound.infrastructure.elastic.repository.TaskElasticRepository;
import clap.server.application.port.outbound.task.ElasticTaskPort;
import clap.server.common.annotation.architecture.InfrastructureAdapter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@InfrastructureAdapter
@RequiredArgsConstructor
public class ElasticTaskAdapter implements ElasticTaskPort {
    private final TaskElasticRepository taskElasticRepository;

    @Override
    public void saveStatistics(List<ElasticTask> Statistics) {
        taskElasticRepository.saveAll(Statistics);
    }
}
