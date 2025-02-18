package clap.server.application.service.task;

import clap.server.adapter.inbound.web.dto.task.request.UpdateTaskOrderRequest;
import clap.server.adapter.outbound.persistense.entity.task.constant.TaskStatus;
import clap.server.application.port.inbound.domain.MemberService;
import clap.server.application.port.inbound.domain.TaskService;
import clap.server.application.port.outbound.task.LoadTaskPort;
import clap.server.domain.model.member.Member;
import clap.server.domain.model.task.Task;
import clap.server.domain.policy.task.ProcessorValidationPolicy;
import clap.server.domain.policy.task.TaskOrderCalculationPolicy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
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

    @Test
    @DisplayName("작업 순서를 업데이트 - 가장 상위로 이동")
    void updateTaskOrder_moveToTop() {
        // given
        Long processorId = 1L;
        UpdateTaskOrderRequest request = new UpdateTaskOrderRequest(0L, 2L, 1L); // prevTaskId = 0 (맨 위로 이동)
        TaskStatus taskStatus = TaskStatus.IN_PROGRESS;

        Member processor = mock(Member.class);
        Task targetTask = mock(Task.class);
        Task nextTask = mock(Task.class);
        Task prevTask = mock(Task.class);

        when(memberService.findActiveMemberWithDepartment(processorId)).thenReturn(processor);
        when(taskService.findById(request.targetTaskId())).thenReturn(targetTask);
        when(targetTask.getTaskStatus()).thenReturn(taskStatus);
        when(taskService.findByIdAndStatus(request.nextTaskId(), taskStatus)).thenReturn(nextTask);
        when(loadTaskPort.findPrevOrderTaskByProcessorOrderAndStatus(processorId, taskStatus, nextTask.getProcessorOrder()))
                .thenReturn(Optional.of(prevTask));
        when(taskOrderCalculationPolicy.calculateOrderForTop(prevTask, nextTask)).thenReturn(100L);

        // when
        updateTaskBoardService.updateTaskOrder(processorId, request);

        // then
        verify(targetTask).updateProcessorOrder(100L);
        verify(taskService).upsert(targetTask);
        verify(processorValidationPolicy).validateProcessor(processorId, targetTask);
    }

    @Test
    @DisplayName("작업 순서를 업데이트 - 가장 하위로 이동")
    void updateTaskOrder_moveToBottom() {
        // given
        Long processorId = 1L;
        UpdateTaskOrderRequest request = new UpdateTaskOrderRequest(1L, 2L, 0L); // nextTaskId = 0 (맨 아래로 이동)
        TaskStatus taskStatus = TaskStatus.IN_PROGRESS;

        Member processor = mock(Member.class);
        Task targetTask = mock(Task.class);
        Task prevTask = mock(Task.class);
        Task nextTask = mock(Task.class);

        when(memberService.findActiveMemberWithDepartment(processorId)).thenReturn(processor);
        when(taskService.findById(request.targetTaskId())).thenReturn(targetTask);
        when(targetTask.getTaskStatus()).thenReturn(taskStatus);
        when(taskService.findByIdAndStatus(request.prevTaskId(), taskStatus)).thenReturn(prevTask);
        when(loadTaskPort.findNextOrderTaskByProcessorOrderAndStatus(processorId, taskStatus, prevTask.getProcessorOrder()))
                .thenReturn(Optional.of(nextTask));
        when(taskOrderCalculationPolicy.calculateOrderForBottom(prevTask, nextTask)).thenReturn(200L);

        // when
        updateTaskBoardService.updateTaskOrder(processorId, request);

        // then
        verify(targetTask).updateProcessorOrder(200L);
        verify(taskService).upsert(targetTask);
        verify(processorValidationPolicy).validateProcessor(processorId, targetTask);
    }



    @Test
    @DisplayName("작업 순서를 업데이트 - 중간으로 이동")
    void updateTaskOrder_moveToMiddle() {
        // given
        Long processorId = 1L;
        UpdateTaskOrderRequest request = new UpdateTaskOrderRequest(2L, 3L, 1L); // prev와 next 둘 다 존재 (중간 이동)
        TaskStatus taskStatus = TaskStatus.IN_PROGRESS;

        Member processor = mock(Member.class);
        Task targetTask = mock(Task.class);
        Task prevTask = mock(Task.class);
        Task nextTask = mock(Task.class);

        when(memberService.findActiveMemberWithDepartment(processorId)).thenReturn(processor);
        when(taskService.findById(request.targetTaskId())).thenReturn(targetTask);
        when(targetTask.getTaskStatus()).thenReturn(taskStatus);
        when(taskService.findByIdAndStatus(request.prevTaskId(), taskStatus)).thenReturn(prevTask);
        when(taskService.findByIdAndStatus(request.nextTaskId(), taskStatus)).thenReturn(nextTask);
        when(prevTask.getProcessorOrder()).thenReturn(100L);
        when(nextTask.getProcessorOrder()).thenReturn(200L);
        when(taskOrderCalculationPolicy.calculateNewProcessorOrder(prevTask.getProcessorOrder(), nextTask.getProcessorOrder()))
                .thenReturn(150L);

        // when
        updateTaskBoardService.updateTaskOrder(processorId, request);

        // then
        verify(targetTask).updateProcessorOrder(150L);
        verify(taskService).upsert(targetTask);
        verify(processorValidationPolicy).validateProcessor(processorId, targetTask);
    }
}
