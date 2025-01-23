package clap.server.adapter.outbound.infrastructure.elastic;

import clap.server.adapter.outbound.infrastructure.elastic.document.TaskDocument;
import clap.server.adapter.outbound.infrastructure.elastic.repository.TaskElasticRepository;
import clap.server.application.port.outbound.task.TaskDocumentPort;
import clap.server.common.annotation.architecture.InfrastructureAdapter;
import clap.server.domain.model.task.Task;
import co.elastic.clients.elasticsearch._types.aggregations.AggregationBuilders;
import co.elastic.clients.elasticsearch._types.aggregations.MultiBucketBase;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchAggregations;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@InfrastructureAdapter
@RequiredArgsConstructor
public class TaskDocumentAdapter implements TaskDocumentPort {
    private final TaskElasticRepository taskElasticRepository;
    private final ElasticsearchOperations elasticsearchOperations;

    @Override
    public void saveStatistics(List<Task> statistics) {
        taskElasticRepository.saveAll(statistics.stream().map(TaskDocument::new).toList());
    }

    @Override
    public Map<String, Long> findPeriodTaskRequestByPeriod(String period) {
        PeriodConfig periodConfig = PeriodConfig.valueOf(period.toUpperCase());

        NativeQuery query = buildPeriodTaskRequestQuery(periodConfig);
        return getPeriodTaskResults(executeQuery(query), periodConfig);
    }

    @Override
    public Map<String, Long> findPeriodTaskProcessByPeriod(String period) {
        PeriodConfig periodConfig = PeriodConfig.valueOf(period.toUpperCase());

        NativeQuery query = buildPeriodTaskProcessQuery(periodConfig);
        return getPeriodTaskResults(executeQuery(query), periodConfig);
    }

    @Override
    public Map<String, Long> findCategoryTaskRequestByPeriod(String period) {
        PeriodConfig periodConfig = PeriodConfig.valueOf(period.toUpperCase());

        NativeQuery query = buildCategoryTaskRequestQuery(periodConfig);
        return getCategoryTaskResults(executeQuery(query));
    }

    @Override
    public Map<String, Long> findSubCategoryTaskRequestByPeriod(String period, String mainCategory) {
        PeriodConfig periodConfig = PeriodConfig.valueOf(period.toUpperCase());

        NativeQuery query = buildSubCategoryTaskRequestQuery(periodConfig, mainCategory);
        return getCategoryTaskResults(executeQuery(query));
    }

    @Override
    public Map<String, Long> findManagerTaskProcessByPeriod(String period) {
        PeriodConfig periodConfig = PeriodConfig.valueOf(period.toUpperCase());

        NativeQuery query = buildManagerTaskProcessQuery(periodConfig);
        return getManagerTaskResults(executeQuery(query));
    }

    private NativeQuery buildPeriodTaskRequestQuery(PeriodConfig config) {
        return NativeQuery.builder()
                .withQuery(q -> q
                        .range(r -> r
                                .date(d -> d
                                        .field("created_at")
                                        .gte(String.valueOf(LocalDate.now().minusDays(config.getDaysToSubtract()))))))
                .withAggregation("period_task", AggregationBuilders.dateHistogram()
                        .field("created_at")
                        .calendarInterval(config.getCalendarInterval())
                        .build()._toAggregation())
                .withMaxResults(0)
                .build();
    }

    private NativeQuery buildPeriodTaskProcessQuery(PeriodConfig config) {
        NativeQuery rangeQuery = NativeQuery.builder()
                .withQuery(q -> q
                        .range(r -> r
                                .date(d -> d
                                        .field("created_at")
                                        .gte(String.valueOf(LocalDate.now().minusDays(config.getDaysToSubtract())))))).build();
        NativeQuery statusQuery = NativeQuery.builder()
                .withQuery(q -> q
                        .term(v -> v
                                .field("status")
                                .value("completed"))).build();

        return NativeQuery.builder()
                .withQuery(q -> q
                        .bool(b -> b
                                .must(rangeQuery.getQuery(), statusQuery.getQuery()))
                )
                .withAggregation("period_task", AggregationBuilders.dateHistogram()
                        .field("created_at")
                        .calendarInterval(config.getCalendarInterval())
                        .build()._toAggregation())
                .withMaxResults(0)
                .build();
    }

    private NativeQuery buildCategoryTaskRequestQuery(PeriodConfig config) {
        return NativeQuery.builder()
                .withQuery(q -> q
                        .range(r -> r
                                .date(d -> d
                                        .field("created_at")
                                        .gte(String.valueOf(LocalDate.now().minusDays(config.getDaysToSubtract()))))))
                .withAggregation("category_task", AggregationBuilders.terms()
                        .field("main_category")
                        .build()._toAggregation())
                .withMaxResults(0)
                .build();
    }

    private NativeQuery buildSubCategoryTaskRequestQuery(PeriodConfig config, String mainCategory) {
        NativeQuery rangeQuery = NativeQuery.builder()
                .withQuery(q -> q
                        .range(r -> r
                                .date(d -> d
                                        .field("created_at")
                                        .gte(String.valueOf(LocalDate.now().minusDays(config.getDaysToSubtract())))))).build();
        NativeQuery categoryQuery = NativeQuery.builder()
                .withQuery(q -> q
                        .term(v -> v
                                .field("main_category")
                                .value(mainCategory))).build();

        return NativeQuery.builder()
                .withQuery(q -> q
                        .bool(b -> b
                                .must(rangeQuery.getQuery(), categoryQuery.getQuery()))
                )
                .withAggregation("category_task", AggregationBuilders.terms()
                        .field("sub_category")
                        .build()._toAggregation())
                .withMaxResults(0)
                .build();
    }

    private NativeQuery buildManagerTaskProcessQuery(PeriodConfig config) {
        return NativeQuery.builder()
                .withQuery(q -> q
                        .range(r -> r
                                .date(d -> d
                                        .field("created_at")
                                        .gte(String.valueOf(LocalDate.now().minusDays(config.getDaysToSubtract()))))))
                .withAggregation("manager_task", AggregationBuilders.terms()
                        .field("processor")
                        .build()._toAggregation())
                .withMaxResults(0)
                .build();
    }

    private ElasticsearchAggregations executeQuery(NativeQuery query) {
        return (ElasticsearchAggregations) elasticsearchOperations
                .search(query, TaskDocument.class)
                .getAggregations();
    }

    private Map<String, Long> getPeriodTaskResults(ElasticsearchAggregations aggregations, PeriodConfig config) {
        return new TreeMap<>(
                aggregations.get("period_task")
                        .aggregation()
                        .getAggregate()
                        .dateHistogram()
                        .buckets()
                        .array()
                        .stream()
                        .collect(Collectors.toMap(
                                bucket -> bucket.keyAsString().substring(
                                        config.getSubstringStart(),
                                        config.getSubstringEnd()
                                ),
                                MultiBucketBase::docCount
                        ))
        );
    }

    private Map<String, Long> getCategoryTaskResults(ElasticsearchAggregations aggregations) {
        return new TreeMap<>(
                aggregations.get("category_task")
                        .aggregation()
                        .getAggregate()
                        .sterms()
                        .buckets()
                        .array()
                        .stream()
                        .collect(Collectors.toMap(
                                bucket -> bucket.key().stringValue(),
                                MultiBucketBase::docCount
                        ))
        );
    }

    private Map<String, Long> getManagerTaskResults(ElasticsearchAggregations aggregations) {
        return new TreeMap<>(
                aggregations.get("manager_task")
                        .aggregation()
                        .getAggregate()
                        .sterms()
                        .buckets()
                        .array()
                        .stream()
                        .collect(Collectors.toMap(
                                bucket -> bucket.key().stringValue(),
                                MultiBucketBase::docCount
                        ))
        );
    }
}
