package clap.server.application.port.outbound.task;

import clap.server.adapter.outbound.infrastructure.elastic.entity.ElasticTask;

import java.util.List;

public interface ElasticTaskPort {
    void saveStatistics(List<ElasticTask> Statistics);
}
