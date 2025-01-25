package clap.server.task;

import clap.server.adapter.inbound.web.dto.task.FilterPendingApprovalResponse;
import clap.server.adapter.inbound.web.dto.task.FilterTaskListRequest;
import clap.server.adapter.inbound.web.dto.task.OrderRequest;

import clap.server.application.Task.FindTaskListService;
import clap.server.application.port.inbound.domain.MemberService;
import clap.server.application.port.outbound.task.LoadTaskPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindTaskListServiceTest {

    @Mock
    private MemberService memberService;
    @Mock
    private LoadTaskPort loadTaskPort;
    @InjectMocks
    private FindTaskListService findTaskListService;

    private FilterTaskListRequest filterTaskListRequest;
    private Pageable pageable;
    private Page<FilterPendingApprovalResponse> expectedResponse;

    @BeforeEach
    void setUp() {
        pageable = PageRequest.of(0, 20);
        filterTaskListRequest = new FilterTaskListRequest(
                null, List.of(2L), List.of(1L), "작업 제목", "", List.of(), new OrderRequest("REQUESTED_AT", "DESC")
        );

        FilterPendingApprovalResponse response = new FilterPendingApprovalResponse(
                1L, "TC001", LocalDateTime.of(2025, 1, 24, 12, 30),
                "메인 카테고리", "서브 카테고리", "작업 제목", "atom.park"
        );

        FilterPendingApprovalResponse response2 = new FilterPendingApprovalResponse(
                2L, "TC002", LocalDateTime.of(2025, 1, 15, 14, 30),
                "메인 카테고리2", "서브 카테고리2", "다른 작업 제목", "john.doe"
        );
        expectedResponse = new PageImpl<>(List.of(response, response2), pageable, 1);
    }

    @Test
    @DisplayName("승인대기 중인 작업요청목록 조회")
    void findPendingApprovalTasks_ReturnFilteredTasks() {
        // given
        Long managerId = 1L;
        when(loadTaskPort.findPendingApprovalTasks(pageable, filterTaskListRequest))
                .thenReturn(expectedResponse);

        // when
        Page<FilterPendingApprovalResponse> result = findTaskListService.findPendingApprovalTasks(managerId, pageable, filterTaskListRequest);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(2);

        assertThat(result.getContent()).hasSize(2)
                .extracting(FilterPendingApprovalResponse::taskId)
                .containsExactly(1L, 2L);

        assertThat(result.getContent().get(0))
                .extracting(FilterPendingApprovalResponse::taskId, FilterPendingApprovalResponse::taskCode,
                        FilterPendingApprovalResponse::requestedAt, FilterPendingApprovalResponse::mainCategoryName,
                        FilterPendingApprovalResponse::categoryName, FilterPendingApprovalResponse::title,
                        FilterPendingApprovalResponse::requesterName)
                .containsExactly(1L, "TC001", LocalDateTime.of(2025, 1, 24, 12, 30),
                        "메인 카테고리", "서브 카테고리", "작업 제목", "atom.park");

        assertThat(result.getContent().get(1))
                .extracting(FilterPendingApprovalResponse::taskId, FilterPendingApprovalResponse::taskCode,
                        FilterPendingApprovalResponse::requestedAt, FilterPendingApprovalResponse::mainCategoryName,
                        FilterPendingApprovalResponse::categoryName, FilterPendingApprovalResponse::title,
                        FilterPendingApprovalResponse::requesterName)
                .containsExactly(2L, "TC002", LocalDateTime.of(2025, 1, 15, 14, 30),
                        "메인 카테고리2", "서브 카테고리2", "다른 작업 제목", "john.doe");

        verify(loadTaskPort).findPendingApprovalTasks(pageable, filterTaskListRequest);
    }
}
