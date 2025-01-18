package clap.server.adapter.outbound.infrastructure.elastic.repository;

import clap.server.adapter.outbound.infrastructure.elastic.document.ElasticTask;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface TaskElasticRepository extends ElasticsearchRepository<ElasticTask, Long> {
}
