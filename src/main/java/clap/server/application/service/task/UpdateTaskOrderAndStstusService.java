package clap.server.application.service.task;

import clap.server.adapter.inbound.web.dto.task.request.UpdateTaskOrderRequest;
import clap.server.adapter.outbound.persistense.entity.member.constant.MemberRole;
import clap.server.adapter.outbound.persistense.entity.notification.constant.NotificationType;
import clap.server.adapter.outbound.persistense.entity.task.constant.TaskHistoryType;
import clap.server.adapter.outbound.persistense.entity.task.constant.TaskStatus;
import clap.server.application.port.inbound.domain.MemberService;
import clap.server.application.port.inbound.domain.TaskService;
import clap.server.application.port.inbound.task.UpdateTaskOrderAndStatusUsecase;
import clap.server.application.port.outbound.task.LoadTaskPort;
import clap.server.application.port.outbound.taskhistory.CommandTaskHistoryPort;
import clap.server.application.service.webhook.SendNotificationService;
import clap.server.common.annotation.architecture.ApplicationService;
import clap.server.domain.model.member.Member;
import clap.server.domain.model.task.Task;
import clap.server.domain.model.task.TaskHistory;
import clap.server.domain.policy.task.ProcessorValidationPolicy;
import clap.server.domain.policy.task.TaskOrderCalculationPolicy;
import clap.server.domain.policy.task.TaskPolicyConstants;
import clap.server.exception.ApplicationException;
import clap.server.exception.code.TaskErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@ApplicationService
@RequiredArgsConstructor
public class UpdateTaskOrderAndStstusService implements UpdateTaskOrderAndStatusUsecase {
    private final MemberService memberService;
    private final TaskService taskService;
    private final LoadTaskPort loadTaskPort;
    private final SendNotificationService sendNotificationService;
    private final CommandTaskHistoryPort commandTaskHistoryPort;

    private final UpdateProcessorTaskCountService updateProcessorTaskCountService;
    private final TaskOrderCalculationPolicy taskOrderCalculationPolicy;
    private final ProcessorValidationPolicy processorValidationPolicy;

    /**
     * 작업의 상태와 순서를 동시에 변경하는 메서드
     *
     * @param processorId  작업을 수행하는 멤버 ID
     * @param request      순서 변경 요청 객체
     * @param targetStatus 변경할 작업 상태
     */
    @Override
    @Transactional
    public void updateTaskOrderAndStatus(Long processorId, UpdateTaskOrderRequest request, TaskStatus targetStatus) {
        validateRequest(targetStatus);
        Member processor = memberService.findActiveMemberWithDepartment(processorId);
        Task targetTask = taskService.findById(request.targetTaskId());
        processorValidationPolicy.validateProcessor(processorId, targetTask);

        Task updatedTask;
        Task prevTask;
        Task nextTask;
        updateProcessorTaskCountService.handleTaskStatusChange(processor, targetTask.getTaskStatus(), targetStatus);

        if (request.prevTaskId() == 0 && request.nextTaskId() == 0) {
            updatedTask = handleSingleTask(processorId, targetStatus, targetTask);
        } else if (request.prevTaskId() == 0) {
            nextTask = taskService.findByIdAndStatus(request.nextTaskId(), targetStatus);
            // 해당 상태에서 바로 앞 있는 작업 찾기
            prevTask = loadTaskPort.findPrevOrderTaskByProcessorOrderAndStatus(processorId, targetStatus, nextTask.getProcessorOrder()).orElse(null);
            long newOrder = taskOrderCalculationPolicy.calculateOrderForTop(prevTask, nextTask);
            updatedTask = updateNewTaskOrderAndStatus(targetStatus, targetTask, newOrder);
        } else if (request.nextTaskId() == 0) {
            prevTask = taskService.findByIdAndStatus(request.prevTaskId(), targetStatus);
            // 해당 상태에서 바로 뒤에 있는 작업 찾기
            nextTask = loadTaskPort.findNextOrderTaskByProcessorOrderAndStatus(processorId, targetStatus, prevTask.getProcessorOrder()).orElse(null);
            long newOrder = taskOrderCalculationPolicy.calculateOrderForBottom(prevTask, nextTask);
            updatedTask = updateNewTaskOrderAndStatus(targetStatus, targetTask, newOrder);
        } else {
            prevTask = taskService.findByIdAndStatus(request.prevTaskId(), targetStatus);
            nextTask = taskService.findByIdAndStatus(request.nextTaskId(), targetStatus);
            long newOrder = taskOrderCalculationPolicy.calculateNewProcessorOrder(prevTask.getProcessorOrder(), nextTask.getProcessorOrder());
            updatedTask = updateNewTaskOrderAndStatus(targetStatus, targetTask, newOrder);
        }

        TaskHistory taskHistory = TaskHistory.createTaskHistory(TaskHistoryType.STATUS_SWITCHED, updatedTask, targetStatus.getDescription(), null);
        commandTaskHistoryPort.save(taskHistory);
        publishNotification(targetTask);
    }

    /**
     * 작업 보드에 조회된 하나의 task를 이동하는 메서드
     */
    private Task handleSingleTask(Long processorId, TaskStatus targetStatus, Task targetTask) {
        Task updatedTask;
        Task nextTask;
        Task prevTask;

        // 요청 시간 기준으로 가장 가장 근접한 이전의 Task를 조회
        prevTask = loadTaskPort.findPrevOrderTaskByTaskIdAndStatus(processorId, targetStatus, targetTask.getTaskId()).orElse(null);
        if (prevTask != null) {
            // 이전 Task가 있다면 바로 다음의 Task 조회
            nextTask = loadTaskPort.findNextOrderTaskByProcessorOrderAndStatus(processorId, targetStatus, prevTask.getProcessorOrder()).orElse(null);
        } // 요청 시간 기준으로 가장 가장 근접한 이후의 Task를 조회
        else
            nextTask = loadTaskPort.findNextOrderTaskByTaskIdAndStatus(processorId, targetStatus, targetTask.getTaskId()).orElse(null);

        // 하나의 task만 존재할 경우 상태만 update
        if (prevTask == null && nextTask == null) {
            targetTask.updateTaskStatus(targetStatus);
            updatedTask = taskService.upsert(targetTask);
        } else if (prevTask == null) {
            long newOrder = taskOrderCalculationPolicy.calculateOrderForTop(null, nextTask);
            updatedTask = updateNewTaskOrderAndStatus(targetStatus, targetTask, newOrder);
        } else if (nextTask == null) {
            long newOrder = taskOrderCalculationPolicy.calculateOrderForBottom(prevTask, null);
            updatedTask = updateNewTaskOrderAndStatus(targetStatus, targetTask, newOrder);
        } else {
            long newOrder = taskOrderCalculationPolicy.calculateNewProcessorOrder(prevTask.getProcessorOrder(), nextTask.getProcessorOrder());
            updatedTask = updateNewTaskOrderAndStatus(targetStatus, targetTask, newOrder);
        }
        return updatedTask;
    }

    /**
     * 작업의 상태와 순서를 업데이트하는 메서드
     */
    private Task updateNewTaskOrderAndStatus(TaskStatus targetStatus, Task targetTask, long newOrder) {
        targetTask.updateProcessorOrder(newOrder);
        targetTask.updateTaskStatus(targetStatus);
        return taskService.upsert(targetTask);
    }

    /**
     * 순서 변경 요청의 유효성을 검증하는 메서드
     */
    public void validateRequest(TaskStatus targetStatus) {
        // 타겟 상태가 유효한지 검증
        if (targetStatus != null && !TaskPolicyConstants.TASK_BOARD_STATUS_FILTER.contains(targetStatus)) {
            throw new ApplicationException(TaskErrorCode.INVALID_TASK_STATUS_TRANSITION);
        }
    }

    private void publishNotification(Task task) {
        List<Member> receivers = List.of(task.getRequester());
        receivers.forEach(receiver -> {
            boolean isManager = receiver.getMemberInfo().getRole() == MemberRole.ROLE_MANAGER;
            sendNotificationService.sendPushNotification(receiver, NotificationType.STATUS_SWITCHED, task, task.getTaskStatus().getDescription(), null, null, isManager);
        });
        sendNotificationService.sendAgitNotification(NotificationType.STATUS_SWITCHED,
                task, task.getTaskStatus().getDescription(), null);
    }

}

