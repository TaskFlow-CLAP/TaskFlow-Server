package clap.server.application.statistics;

import clap.server.adapter.inbound.web.dto.statistics.StatisticsResponse;
import clap.server.application.port.outbound.task.TaskDocumentPort;
import clap.server.application.service.statistics.FindTaskProcessService;
import clap.server.domain.policy.task.TaskStatisticsPolicy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindTaskProcessServiceTest {
    @Mock
    private TaskDocumentPort taskDocumentPort;
    @Mock
    private TaskStatisticsPolicy taskStatisticsPolicy;
    @InjectMocks
    private FindTaskProcessService findTaskProcessService;

    @Test
    @DisplayName("카테고리별 요청량")
    void aggregateCategoryTaskRequest() {
        //given
        Map<String, Long> statistics = new TreeMap<>();
        statistics.put("VM", 6L);
        statistics.put("KS", 7L);
        statistics.put("DN", 8L);

        when(taskDocumentPort.findCategoryTaskRequestByPeriod(eq("week"))).thenReturn(statistics);

        //when
        List<StatisticsResponse> week = findTaskProcessService.aggregateCategoryTaskRequest("week");

        //then
        assertThat(week.get(0).key()).isEqualTo("DN");
        assertThat(week.get(0).count()).isEqualTo(8L);
        assertThat(week.get(1).key()).isEqualTo("KS");
        assertThat(week.get(1).count()).isEqualTo(7L);
        assertThat(week.get(2).key()).isEqualTo("VM");
        assertThat(week.get(2).count()).isEqualTo(6L);
    }

    @Test
    @DisplayName("담당자별 처리량")
    void aggregateManagerTaskProcess() {
        //given
        Map<String, Long> statistics = new TreeMap<>();
        statistics.put("log.d", 6L);
        statistics.put("tony.tsx", 7L);
        statistics.put("moya.moya", 8L);

        when(taskDocumentPort.findManagerTaskProcessByPeriod(eq("week"))).thenReturn(statistics);

        //when
        List<StatisticsResponse> week = findTaskProcessService.aggregateManagerTaskProcess("week");

        //then
        assertThat(week.get(0).key()).isEqualTo("log.d");
        assertThat(week.get(0).count()).isEqualTo(6L);
        assertThat(week.get(1).key()).isEqualTo("moya.moya");
        assertThat(week.get(1).count()).isEqualTo(8L);
        assertThat(week.get(2).key()).isEqualTo("tony.tsx");
        assertThat(week.get(2).count()).isEqualTo(7L);
    }

    @Test
    @DisplayName("기간별 처리량")
    void aggregatePeriodTaskProcess() {
        //given
        Map<String, Long> statistics = new TreeMap<>();
        statistics.put("2025-02-01", 8L);
        statistics.put("2025-01-30", 7L);
        statistics.put("2025-01-31", 6L);

        when(taskDocumentPort.findPeriodTaskProcessByPeriod(eq("week"))).thenReturn(statistics);

        Map<String, Long> formattedStatistics = new TreeMap<>();
        formattedStatistics.put("1월 30일", 7L);
        formattedStatistics.put("1월 31일", 6L);
        when(taskStatisticsPolicy.filterAndFormatWeekdayStatistics(statistics)).thenReturn(formattedStatistics);

        //when
        List<StatisticsResponse> week = findTaskProcessService.aggregatePeriodTaskProcess("week");

        //then
        assertThat(week.get(0).key()).isEqualTo("1월 30일");
        assertThat(week.get(0).count()).isEqualTo(7L);
        assertThat(week.get(1).key()).isEqualTo("1월 31일");
        assertThat(week.get(1).count()).isEqualTo(6L);
    }

    @Test
    @DisplayName("기간별 요청량")
    void aggregatePeriodTaskRequest() {
        //given
        Map<String, Long> statistics = new TreeMap<>();
        statistics.put("2025-02-01", 8L);
        statistics.put("2025-01-30", 7L);
        statistics.put("2025-01-31", 6L);

        when(taskDocumentPort.findPeriodTaskRequestByPeriod(eq("week"))).thenReturn(statistics);

        Map<String, Long> formattedStatistics = new TreeMap<>();
        formattedStatistics.put("1월 30일", 7L);
        formattedStatistics.put("1월 31일", 6L);
        when(taskStatisticsPolicy.filterAndFormatWeekdayStatistics(statistics)).thenReturn(formattedStatistics);

        //when
        List<StatisticsResponse> week = findTaskProcessService.aggregatePeriodTaskRequest("week");

        //then
        assertThat(week.get(0).key()).isEqualTo("1월 30일");
        assertThat(week.get(0).count()).isEqualTo(7L);
        assertThat(week.get(1).key()).isEqualTo("1월 31일");
        assertThat(week.get(1).count()).isEqualTo(6L);
    }
}