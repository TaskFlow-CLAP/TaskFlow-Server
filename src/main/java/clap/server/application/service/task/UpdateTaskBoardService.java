package clap.server.application.service.task;

import clap.server.adapter.inbound.web.dto.task.request.UpdateTaskOrderRequest;
import clap.server.adapter.outbound.persistense.entity.notification.constant.NotificationType;
import clap.server.adapter.outbound.persistense.entity.task.constant.TaskHistoryType;
import clap.server.adapter.outbound.persistense.entity.task.constant.TaskStatus;
import clap.server.application.port.inbound.domain.MemberService;
import clap.server.application.port.inbound.domain.TaskService;
import clap.server.application.port.inbound.task.UpdateTaskBoardUsecase;
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

import java.util.List;

@Slf4j
@ApplicationService
@RequiredArgsConstructor
class UpdateTaskBoardService implements UpdateTaskBoardUsecase, UpdateTaskOrderAndStatusUsecase {
    private final MemberService memberService;
    private final TaskService taskService;
    private final LoadTaskPort loadTaskPort;
    private final SendNotificationService sendNotificationService;
    private final CommandTaskHistoryPort commandTaskHistoryPort;

    private final TaskOrderCalculationPolicy taskOrderCalculationPolicy;
    private final ProcessorValidationPolicy processorValidationPolicy;

    private Task findByIdAndStatus(Long taskId, TaskStatus status) {
        return loadTaskPort.findByIdAndStatus(taskId, status).orElseThrow(() -> new ApplicationException(TaskErrorCode.TASK_NOT_FOUND));
    }

    /**
     * 작업(Task)의 순서를 업데이트하는 메서드
     *
     * @param processorId 작업을 수행하는 멤버 ID
     * @param request     순서 변경 요청 객체
     */
    @Override
    @Transactional
    public void updateTaskOrder(Long processorId, UpdateTaskOrderRequest request) {
        // 요청 유효성 검증
        validateRequest(request, null);
        Member processor = memberService.findActiveMember(processorId);
        Task targetTask = taskService.findById(request.targetTaskId());
        processorValidationPolicy.validateProcessor(processorId, targetTask);

        // 가장 상위로 이동
        if (request.prevTaskId() == 0) {
            Task nextTask = findByIdAndStatus(request.nextTaskId(), targetTask.getTaskStatus());
            // 해당 상태에서 바로 앞에 있는 작업 찾기
            Task prevTask = loadTaskPort.findPrevOrderTaskByProcessorOrderAndStatus(processorId, targetTask.getTaskStatus(), nextTask.getProcessorOrder()).orElse(null);
            long newOrder = taskOrderCalculationPolicy.calculateOrderForTop(prevTask, nextTask);
            updateNewTaskOrder(targetTask, newOrder);
        }
        // 가장 하위로 이동
        else if (request.nextTaskId() == 0) {
            Task prevTask = findByIdAndStatus(request.prevTaskId(), targetTask.getTaskStatus());
            // 해당 상태에서 바로 뒤에 있는 작업 찾기
            Task nextTask = loadTaskPort.findNextOrderTaskByProcessorOrderAndStatus(processorId, targetTask.getTaskStatus(), prevTask.getProcessorOrder()).orElse(null);
            long newOrder = taskOrderCalculationPolicy.calculateOrderForBottom(prevTask, nextTask);
            updateNewTaskOrder(targetTask, newOrder);
        } else {
            Task prevTask = findByIdAndStatus(request.prevTaskId(), targetTask.getTaskStatus());
            Task nextTask = findByIdAndStatus(request.nextTaskId(), targetTask.getTaskStatus());
            long newOrder = taskOrderCalculationPolicy.calculateNewProcessorOrder(prevTask.getProcessorOrder(), nextTask.getProcessorOrder());
            updateNewTaskOrder(targetTask, newOrder);
        }
    }


    /**
     * 새로운 processorOrder를 계산하고 저장하는 메서드
     *
     * @param targetTask 순서를 변경할 대상 작업
     */
    private void updateNewTaskOrder(Task targetTask, Long newOrder) {
        targetTask.updateProcessorOrder(newOrder);
        taskService.upsert(targetTask);
    }

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
        validateRequest(request, targetStatus);
        Member processor = memberService.findActiveMember(processorId);
        Task targetTask = taskService.findById(request.targetTaskId());
        processorValidationPolicy.validateProcessor(processorId, targetTask);

        Task updatedTask;
        Task prevTask;
        Task nextTask;

        // 조회된 작업 보드에서 하나의 작업만 존재하고, 이 작업을 이동할 때
        if (request.prevTaskId() == 0 && request.nextTaskId() == 0) {

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
                long newOrder = taskOrderCalculationPolicy.calculateOrderForBottom(null, nextTask);
                updatedTask = updateNewTaskOrderAndStatus(targetStatus, targetTask, newOrder);
            } else if (nextTask == null) {
                long newOrder = taskOrderCalculationPolicy.calculateOrderForBottom(prevTask, null);
                updatedTask = updateNewTaskOrderAndStatus(targetStatus, targetTask, newOrder);
            } else {
                long newOrder = taskOrderCalculationPolicy.calculateNewProcessorOrder(prevTask.getProcessorOrder(), nextTask.getProcessorOrder());
                updatedTask = updateNewTaskOrderAndStatus(targetStatus, targetTask, newOrder);
            }
        } else if (request.prevTaskId() == 0) {
            nextTask = findByIdAndStatus(request.nextTaskId(), targetStatus);
            // 해당 상태에서 바로 앞 있는 작업 찾기
            prevTask = loadTaskPort.findPrevOrderTaskByProcessorOrderAndStatus(processorId, targetStatus, nextTask.getProcessorOrder()).orElse(null);
            long newOrder = taskOrderCalculationPolicy.calculateOrderForTop(prevTask, nextTask);
            updatedTask = updateNewTaskOrderAndStatus(targetStatus, targetTask, newOrder);
        } else if (request.nextTaskId() == 0) {
            prevTask = findByIdAndStatus(request.prevTaskId(), targetStatus);
            // 해당 상태에서 바로 뒤에 있는 작업 찾기
            nextTask = loadTaskPort.findNextOrderTaskByProcessorOrderAndStatus(processorId, targetStatus, prevTask.getProcessorOrder()).orElse(null);
            long newOrder = taskOrderCalculationPolicy.calculateOrderForBottom(prevTask, nextTask);
            updatedTask = updateNewTaskOrderAndStatus(targetStatus, targetTask, newOrder);
        } else {
            prevTask = findByIdAndStatus(request.prevTaskId(), targetStatus);
            nextTask = findByIdAndStatus(request.nextTaskId(), targetStatus);
            long newOrder = taskOrderCalculationPolicy.calculateNewProcessorOrder(prevTask.getProcessorOrder(), nextTask.getProcessorOrder());
            updatedTask = updateNewTaskOrderAndStatus(targetStatus, targetTask, newOrder);
        }

        TaskHistory taskHistory = TaskHistory.createTaskHistory(TaskHistoryType.STATUS_SWITCHED, updatedTask, targetStatus.getDescription(), null,null);
        commandTaskHistoryPort.save(taskHistory);
        //TODO: 최종 단계에서 주석 처리 해제
        //publishNotification(targetTask, NotificationType.STATUS_SWITCHED, String.valueOf(updatedTask.getTaskStatus()));
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
    public void validateRequest(UpdateTaskOrderRequest request, TaskStatus targetStatus) {
        // 타겟 상태가 유효한지 검증
        if (targetStatus != null && !TaskPolicyConstants.TASK_BOARD_STATUS_FILTER.contains(targetStatus)) {
            throw new ApplicationException(TaskErrorCode.INVALID_TASK_STATUS_TRANSITION);
        }
    }

    private void publishNotification(Task task, NotificationType notificationType, String message, String taskTitle) {
        List<Member> receivers = List.of(task.getRequester(), task.getProcessor());
        receivers.forEach(receiver -> {
            sendNotificationService.sendPushNotification(receiver, receiver.getMemberInfo().getNickname(), notificationType,
                    task, taskTitle, message, null);
        });
        sendNotificationService.sendAgitNotification(notificationType,
                task, message, taskTitle, null);
    }

}

