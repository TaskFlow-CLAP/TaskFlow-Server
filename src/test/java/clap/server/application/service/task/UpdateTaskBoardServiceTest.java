package clap.server.application.service.task;

import clap.server.adapter.inbound.web.dto.task.request.UpdateTaskOrderRequest;
import clap.server.application.port.inbound.domain.MemberService;
import clap.server.application.port.inbound.domain.TaskService;
import clap.server.application.port.outbound.task.LoadTaskPort;
import clap.server.application.service.webhook.SendNotificationService;
import clap.server.domain.model.member.Member;
import clap.server.domain.model.task.Task;
import clap.server.domain.policy.task.ProcessorValidationPolicy;
import clap.server.domain.policy.task.TaskOrderCalculationPolicy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateTaskBoardServiceTest {

    @InjectMocks
    private UpdateTaskBoardService updateTaskBoardService;

    @Mock
    private MemberService memberService;

    @Mock
    private TaskService taskService;

    @Mock
    private LoadTaskPort loadTaskPort;

    @Mock
    private TaskOrderCalculationPolicy taskOrderCalculationPolicy;

    @Mock
    private ProcessorValidationPolicy processorValidationPolicy;

    @Mock
    private SendNotificationService sendNotificationService;

    @BeforeEach
    void setUp() {
        lenient().when(loadTaskPort.findByIdAndStatus(anyLong(), any()))
                .thenReturn(Optional.empty());
        lenient().when(taskOrderCalculationPolicy.calculateOrderForTop(any(), any()))
                .thenReturn(100L);
        lenient().when(taskOrderCalculationPolicy.calculateOrderForBottom(any(), any()))
                .thenReturn(200L);
        lenient().when(taskOrderCalculationPolicy.calculateNewProcessorOrder(anyLong(), anyLong()))
                .thenReturn(150L);
    }

    @Test
    @DisplayName("작업 순서를 업데이트-중간 이동")
    void updateTaskOrder() {
        // given
        Long processorId = 1L;
        UpdateTaskOrderRequest request = new UpdateTaskOrderRequest(2L, 0L, 3L); // prevTaskId = 0 (맨 위로 이동)

        Member processor = mock(Member.class);
        Task targetTask = mock(Task.class);
        Task nextTask = mock(Task.class);

        when(taskService.findById(request.targetTaskId())).thenReturn(targetTask);
        when(memberService.findActiveMember(processorId)).thenReturn(processor);
        when(loadTaskPort.findByIdAndStatus(anyLong(), any())).thenReturn(Optional.of(nextTask));

        // when
        updateTaskBoardService.updateTaskOrder(processorId, request);

        // then
        ArgumentCaptor<Long> orderCaptor = ArgumentCaptor.forClass(Long.class);
        verify(targetTask).updateProcessorOrder(orderCaptor.capture());
        assertEquals(150l, orderCaptor.getValue());

        verify(taskService).upsert(targetTask);
    }

    @Test
    @DisplayName("작업 순서를 업데이트 - 가장 상위로 이동")
    void updateTaskOrder_moveToTop() {
        // given
        Long processorId = 1L;
        UpdateTaskOrderRequest request = new UpdateTaskOrderRequest(0L, 2L, 1L); // prevTaskId = 0 (맨 위로 이동)

        Member processor = mock(Member.class);
        Task targetTask = mock(Task.class);
        Task nextTask = mock(Task.class);

        when(memberService.findActiveMember(processorId)).thenReturn(processor);
        when(taskService.findById(request.targetTaskId())).thenReturn(targetTask);
        when(loadTaskPort.findByIdAndStatus(request.nextTaskId(), targetTask.getTaskStatus())).thenReturn(Optional.of(nextTask));

        // Mock nextTask의 processorOrder 값을 반환
        when(nextTask.getProcessorOrder()).thenReturn(200L);

        when(taskOrderCalculationPolicy.calculateOrderForTop(null, nextTask)).thenReturn(100L);

        // when
        updateTaskBoardService.updateTaskOrder(processorId, request);

        // then
        ArgumentCaptor<Long> orderCaptor = ArgumentCaptor.forClass(Long.class);
        verify(targetTask).updateProcessorOrder(orderCaptor.capture());
        assertEquals(100L, orderCaptor.getValue()); // 계산된 순서 값이 올바른지 확인

        verify(taskService).upsert(targetTask); // 작업이 저장되었는지 확인
    }



    @Test
    @DisplayName("작업 순서를 업데이트 - 가장 하위로 이동")
    void updateTaskOrder_moveToBottom() {
        // given
        Long processorId = 1L;
        UpdateTaskOrderRequest request = new UpdateTaskOrderRequest(2L, 3L, 0L); // nextTaskId = 0 (맨 아래로 이동)

        Member processor = mock(Member.class);
        Task targetTask = mock(Task.class);
        Task prevTask = mock(Task.class);

        doReturn(processor).when(memberService).findActiveMember(processorId);
        doReturn(targetTask).when(taskService).findById(request.targetTaskId());
        doReturn(Optional.of(prevTask)).when(loadTaskPort).findByIdAndStatus(anyLong(), any());

        // when
        updateTaskBoardService.updateTaskOrder(processorId, request);

        // then
        verify(taskService).upsert(targetTask);
        verify(targetTask).updateProcessorOrder(200L);
    }
}
