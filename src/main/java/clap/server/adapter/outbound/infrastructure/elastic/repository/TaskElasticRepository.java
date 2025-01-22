package clap.server.adapter.outbound.infrastructure.elastic.repository;

import clap.server.adapter.outbound.infrastructure.elastic.document.TaskDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface TaskElasticRepository extends ElasticsearchRepository<TaskDocument, Long> {
}
