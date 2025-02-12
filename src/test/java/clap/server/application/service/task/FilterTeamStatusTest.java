package clap.server.application.service.task;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import clap.server.adapter.inbound.web.dto.task.request.FilterTeamStatusRequest;
import clap.server.adapter.inbound.web.dto.task.request.SortBy;
import clap.server.adapter.inbound.web.dto.task.response.TeamStatusResponse;
import clap.server.adapter.inbound.web.dto.task.response.TeamTaskItemResponse;
import clap.server.adapter.inbound.web.dto.task.response.TeamTaskResponse;
import clap.server.adapter.inbound.web.task.TeamStatusController;
import clap.server.adapter.outbound.persistense.entity.task.constant.LabelColor;
import clap.server.adapter.outbound.persistense.entity.task.constant.TaskStatus;
import clap.server.application.port.inbound.task.FilterTeamStatusUsecase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class FilterTeamStatusTest {

    @Mock
    private FilterTeamStatusUsecase filterTeamStatusUsecase;

    @InjectMocks
    private TeamStatusController teamStatusController;

    private FilterTeamStatusRequest defaultFilterRequest;
    private TeamTaskItemResponse defaultItem;
    private TeamTaskResponse defaultMember;
    private TeamStatusResponse defaultResponse;

    @BeforeEach
    void setUp() {
        defaultFilterRequest = new FilterTeamStatusRequest(
                SortBy.DEFAULT, List.of(10L, 20L), List.of(1L, 2L), "테스트 타이틀"
        );

        defaultItem = new TeamTaskItemResponse(
                1L, "TC001", "테스트 타이틀", "1차 카테고리", "2차 카테고리",
                new TeamTaskItemResponse.LabelInfo("라벨명", LabelColor.BLUE),
                "요청자", "요청자 이미지", "요청자 부서",
                1L, TaskStatus.IN_PROGRESS, LocalDateTime.of(2025, 2, 9, 10, 0)
        );

        defaultMember = new TeamTaskResponse(
                100L, "담당자1", "담당자 이미지", "담당자 부서",
                1, 0, 1, List.of(defaultItem)
        );

        defaultResponse = new TeamStatusResponse(List.of(defaultMember), 1, 0, 1);
    }

    @Test
    @DisplayName("팀 현황 조회 - 정상 응답")
    void filterTeamStatus_Success() {
        when(filterTeamStatusUsecase.filterTeamStatus(defaultFilterRequest)).thenReturn(defaultResponse);

        ResponseEntity<TeamStatusResponse> response = teamStatusController.filterTeamStatus(defaultFilterRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(defaultResponse);
    }

    @Test
    @DisplayName("팀 현황 조회 - 빈 결과")
    void filterTeamStatus_EmptyResult() {
        FilterTeamStatusRequest emptyFilterRequest = new FilterTeamStatusRequest(SortBy.DEFAULT, List.of(), List.of(), "");
        TeamStatusResponse emptyResponse = new TeamStatusResponse(List.of(), 0, 0, 0);

        when(filterTeamStatusUsecase.filterTeamStatus(emptyFilterRequest)).thenReturn(emptyResponse);

        ResponseEntity<TeamStatusResponse> response = teamStatusController.filterTeamStatus(emptyFilterRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(emptyResponse);
    }

    @Test
    @DisplayName("팀 현황 조회 - 기여도순 정렬")
    void filterTeamStatus_SortByContribute() {
        FilterTeamStatusRequest contributeRequest = new FilterTeamStatusRequest(SortBy.CONTRIBUTE, List.of(10L), List.of(1L), "테스트 타이틀");

        LocalDateTime now = LocalDateTime.now();
        TeamTaskItemResponse item1 = new TeamTaskItemResponse(1L, "TC001", "타이틀1", "1차 카테고리", "2차 카테고리",
                new TeamTaskItemResponse.LabelInfo("라벨1", LabelColor.BLUE), "요청자1", "이미지1", "부서1", 1L, TaskStatus.IN_PROGRESS, now);
        TeamTaskItemResponse item2 = new TeamTaskItemResponse(2L, "TC002", "타이틀2", "1차 카테고리", "2차 카테고리",
                new TeamTaskItemResponse.LabelInfo("라벨2", LabelColor.RED), "요청자2", "이미지2", "부서2", 2L, TaskStatus.IN_PROGRESS, now);

        TeamTaskResponse member1 = new TeamTaskResponse(100L, "담당자1", "이미지1", "부서1", 2, 1, 3, List.of(item1, item2));
        TeamTaskResponse member2 = new TeamTaskResponse(200L, "담당자2", "이미지2", "부서2", 1, 0, 1, List.of(item1));

        TeamStatusResponse contributeResponse = new TeamStatusResponse(List.of(member1, member2),
                member1.inProgressTaskCount() + member2.inProgressTaskCount(),
                member1.inReviewingTaskCount() + member2.inReviewingTaskCount(),
                member1.totalTaskCount() + member2.totalTaskCount());

        when(filterTeamStatusUsecase.filterTeamStatus(contributeRequest)).thenReturn(contributeResponse);

        ResponseEntity<TeamStatusResponse> response = teamStatusController.filterTeamStatus(contributeRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(contributeResponse);
    }

    @Test
    @DisplayName("팀 현황 조회 - 카테고리 필터링")
    void filterTeamStatus_CategoryFiltering() {
        FilterTeamStatusRequest categoryFilterRequest = new FilterTeamStatusRequest(SortBy.DEFAULT, List.of(10L), List.of(), "타이틀");

        LocalDateTime now = LocalDateTime.now();
        TeamTaskItemResponse matchingItem = new TeamTaskItemResponse(1L, "TC001", "타이틀 매칭", "1차 카테고리", "2차 카테고리",
                new TeamTaskItemResponse.LabelInfo("라벨1", LabelColor.BLUE), "요청자1", "이미지1", "부서1", 1L, TaskStatus.IN_PROGRESS, now);

        TeamTaskResponse categoryMember = new TeamTaskResponse(300L, "카테고리담당자", "이미지3", "부서3", 1, 0, 1, List.of(matchingItem));

        TeamStatusResponse categoryResponse = new TeamStatusResponse(List.of(categoryMember), 1, 0, 1);
        when(filterTeamStatusUsecase.filterTeamStatus(categoryFilterRequest)).thenReturn(categoryResponse);

        ResponseEntity<TeamStatusResponse> response = teamStatusController.filterTeamStatus(categoryFilterRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(categoryResponse);
    }
}