package clap.server.application.service.task;

import clap.server.adapter.inbound.web.dto.task.request.ApprovalTaskRequest;
import clap.server.adapter.inbound.web.dto.task.response.ApprovalTaskResponse;
import clap.server.adapter.outbound.persistense.entity.task.constant.TaskStatus;
import clap.server.TestDataFactory;
import clap.server.application.port.inbound.domain.CategoryService;
import clap.server.application.port.inbound.domain.LabelService;
import clap.server.application.port.inbound.domain.MemberService;
import clap.server.application.port.inbound.domain.TaskService;
import clap.server.application.port.outbound.taskhistory.CommandTaskHistoryPort;
import clap.server.application.service.webhook.SendNotificationService;
import clap.server.domain.model.member.Member;
import clap.server.domain.model.task.Category;
import clap.server.domain.model.task.Label;
import clap.server.domain.model.task.Task;
import clap.server.domain.model.task.TaskHistory;
import clap.server.domain.policy.task.RequestedTaskUpdatePolicy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ApprovalTaskServiceTest {

    @InjectMocks
    private ApprovalTaskService approvalTaskService;

    @Mock
    private MemberService memberService;

    @Mock
    private TaskService taskService;

    @Mock
    private CategoryService categoryService;

    @Mock
    private LabelService labelService;

    @Mock
    private RequestedTaskUpdatePolicy requestedTaskUpdatePolicy;

    @Mock
    private CommandTaskHistoryPort commandTaskHistoryPort;

    @Mock
    private SendNotificationService sendNotificationService;


    private Member reviewer, processor;
    private Task task;
    private Label label;
    private Category category, mainCategory;

    @BeforeEach
    void setUp() {
        reviewer = TestDataFactory.createManagerWithReviewer();
        processor = TestDataFactory.createManager();
        mainCategory = TestDataFactory.createMainCategory();
        category = TestDataFactory.createCategory(mainCategory);
        label = TestDataFactory.createLabel();
        task = TestDataFactory.createTask( 1L,"TC001", "제목1", TaskStatus.REQUESTED, category, null, processor);
    }

    @Test
    @DisplayName("작업 승인 처리")
    void approvalTask() {
        //given
        Long reviewerId = 2L;
        Long taskId = 1L;
        ApprovalTaskRequest approvalTaskRequest = new ApprovalTaskRequest(2L, 2L, null, null);

        when(memberService.findReviewer(reviewerId)).thenReturn(reviewer);
        when(taskService.findById(taskId)).thenReturn(task);
        when(memberService.findById(approvalTaskRequest.processorId())).thenReturn(processor);
        when(categoryService.findById(approvalTaskRequest.categoryId())).thenReturn(category);
        when(labelService.findById(approvalTaskRequest.labelId())).thenReturn(label);
        when(taskService.upsert(task)).thenReturn(task);

        //when
        ApprovalTaskResponse response = approvalTaskService.approvalTaskByReviewer(reviewerId, taskId, approvalTaskRequest);

        //then
        assertThat(response).isNotNull();
        assertThat(response.taskStatus()).isEqualTo(TaskStatus.IN_PROGRESS);
        assertThat(response.taskId()).isEqualTo(task.getTaskId());
        verify(requestedTaskUpdatePolicy).validateTaskRequested(task);
        verify(commandTaskHistoryPort, times(1)).save(any(TaskHistory.class));
    }
}

