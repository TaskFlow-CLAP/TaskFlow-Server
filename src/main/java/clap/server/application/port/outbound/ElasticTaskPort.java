package clap.server.application.port.outbound;

import clap.server.adapter.outbound.infrastructure.elastic.document.ElasticTask;

import java.util.List;

public interface ElasticTaskPort {
    void saveStatistics(List<ElasticTask> Statistics);
}
