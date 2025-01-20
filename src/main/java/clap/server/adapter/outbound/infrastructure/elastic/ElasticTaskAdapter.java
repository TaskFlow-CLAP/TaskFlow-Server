package clap.server.adapter.outbound.infrastructure.elastic;

import clap.server.adapter.outbound.infrastructure.elastic.dto.PeriodConfig;
import clap.server.adapter.outbound.infrastructure.elastic.entity.ElasticTask;
import clap.server.adapter.outbound.infrastructure.elastic.repository.TaskElasticRepository;
import clap.server.application.port.outbound.task.ElasticTaskPort;
import clap.server.common.annotation.architecture.InfrastructureAdapter;
import co.elastic.clients.elasticsearch._types.aggregations.AggregationBuilders;
import co.elastic.clients.elasticsearch._types.aggregations.CalendarInterval;
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
public class ElasticTaskAdapter implements ElasticTaskPort {
    private final TaskElasticRepository taskElasticRepository;
    private final ElasticsearchOperations elasticsearchOperations;

    @Override
    public void saveStatistics(List<ElasticTask> statistics) {
        taskElasticRepository.saveAll(statistics);
    }

    @Override
    public Map<String, Long> findPeriodTaskRequestByPeriod(String period) {
        PeriodConfig periodConfig = getPeriodConfig(period);

        NativeQuery query = buildPeriodTaskRequestQuery(periodConfig);
        ElasticsearchAggregations result = executeQuery(query);
        return processResults(result, periodConfig);
    }

    @Override
    public Map<String, Long> findPeriodTaskProcessByPeriod(String period) {
        PeriodConfig periodConfig = getPeriodConfig(period);

        NativeQuery query = buildPeriodTaskProcessQuery(periodConfig);
        ElasticsearchAggregations result = executeQuery(query);
        return processResults(result, periodConfig);
    }

    private PeriodConfig getPeriodConfig(String period) {
        if (period.equals("week")) {
            return new PeriodConfig(14, CalendarInterval.Day, 0, 10);
        }
        return new PeriodConfig(1, CalendarInterval.Hour, 11, 19);
    }

    private NativeQuery buildPeriodTaskRequestQuery(PeriodConfig config) {
        return NativeQuery.builder()
                .withQuery(q -> q
                        .range(r -> r
                                .date(d -> d
                                        .field("created_at")
                                        .gte(String.valueOf(LocalDate.now().minusDays(config.daysToSubtract()))))))
                .withAggregation("period_task", AggregationBuilders.dateHistogram()
                        .field("created_at")
                        .calendarInterval(config.calendarInterval())
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
                                        .gte(String.valueOf(LocalDate.now().minusDays(config.daysToSubtract())))))).build();
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
                        .calendarInterval(config.calendarInterval())
                        .build()._toAggregation())
                .withMaxResults(0)
                .build();
    }

    private ElasticsearchAggregations executeQuery(NativeQuery query) {
        return (ElasticsearchAggregations) elasticsearchOperations
                .search(query, ElasticTask.class)
                .getAggregations();
    }

    private Map<String, Long> processResults(ElasticsearchAggregations aggregations, PeriodConfig config) {
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
                                        config.substringStart(),
                                        config.substringEnd()
                                ),
                                MultiBucketBase::docCount
                        ))
        );
    }
}
