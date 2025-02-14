package clap.server.application.service.task;

import clap.server.adapter.inbound.web.dto.task.request.UpdateTaskProcessorRequest;
import clap.server.adapter.outbound.persistense.entity.member.constant.MemberRole;
import clap.server.adapter.outbound.persistense.entity.notification.constant.NotificationType;
import clap.server.adapter.outbound.persistense.entity.task.constant.TaskHistoryType;
import clap.server.adapter.outbound.persistense.entity.task.constant.TaskStatus;
import clap.server.application.port.inbound.domain.MemberService;
import clap.server.application.port.inbound.domain.TaskService;
import clap.server.application.port.inbound.task.UpdateTaskProcessorUsecase;
import clap.server.application.port.inbound.task.UpdateTaskStatusUsecase;
import clap.server.application.port.outbound.taskhistory.CommandTaskHistoryPort;
import clap.server.application.service.webhook.SendNotificationService;
import clap.server.common.annotation.architecture.ApplicationService;
import clap.server.domain.model.member.Member;
import clap.server.domain.model.task.Task;
import clap.server.domain.model.task.TaskHistory;
import clap.server.exception.ApplicationException;
import clap.server.exception.code.TaskErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static clap.server.domain.policy.task.TaskPolicyConstants.REMAINING_TASK_STATUS;
import static clap.server.domain.policy.task.TaskPolicyConstants.TASK_UPDATABLE_STATUS;


@ApplicationService
@RequiredArgsConstructor
@Slf4j
public class UpdateTaskService implements UpdateTaskStatusUsecase, UpdateTaskProcessorUsecase {
    private final MemberService memberService;
    private final TaskService taskService;
    private final SendNotificationService sendNotificationService;
    private final UpdateProcessorTaskCountService updateProcessorTaskCountService;
    private final CommandTaskHistoryPort commandTaskHistoryPort;

    @Override
    @Transactional
    public void updateTaskStatus(Long memberId, Long taskId, TaskStatus targetTaskStatus) {
        memberService.findActiveMember(memberId);
        Task task = taskService.findById(taskId);

        if (!TASK_UPDATABLE_STATUS.contains(targetTaskStatus)) {
            throw new ApplicationException(TaskErrorCode.TASK_STATUS_NOT_ALLOWED);
        }

        if (!task.getTaskStatus().equals(targetTaskStatus)) {
            updateProcessorTaskCountService.handleTaskStatusChange(task.getProcessor(), task.getTaskStatus(), targetTaskStatus);
            task.updateTaskStatus(targetTaskStatus);
            Task updatedTask = taskService.upsert(task);

            saveTaskHistory(TaskHistory.createTaskHistory(TaskHistoryType.STATUS_SWITCHED, task, targetTaskStatus.getDescription(), null, null));

            List<Member> receivers = List.of(task.getRequester());
            publishNotification(receivers, updatedTask, NotificationType.STATUS_SWITCHED, String.valueOf(updatedTask.getTaskStatus()));
        }
    }

    @Transactional
    @Override
    public void updateTaskProcessor(Long taskId, Long memberId, UpdateTaskProcessorRequest request) {
        memberService.findActiveMember(memberId);

        Task task = taskService.findById(taskId);

        Member processor = memberService.findActiveMember(request.processorId());
        if (REMAINING_TASK_STATUS.contains(task.getTaskStatus())) {
            updateProcessorTaskCountService.handleProcessorChange(task.getProcessor(), processor, task.getTaskStatus());
        }

        task.updateProcessor(processor);
        Task updatedTask = taskService.upsert(task);

        saveTaskHistory(TaskHistory.createTaskHistory(TaskHistoryType.PROCESSOR_CHANGED, task, null, processor, null));

        List<Member> receivers = List.of(updatedTask.getRequester());
        publishNotification(receivers, updatedTask, NotificationType.PROCESSOR_CHANGED, processor.getNickname());
    }

    private void saveTaskHistory(TaskHistory taskHistory) {
        commandTaskHistoryPort.save(taskHistory);
    }

    private void publishNotification(List<Member> receivers, Task task, NotificationType notificationType, String message) {
        receivers.forEach(receiver -> {
            boolean isManager = receiver.getMemberInfo().getRole() == MemberRole.ROLE_MANAGER;
            sendNotificationService.sendPushNotification(receiver, notificationType,
                    task, message, null, null, isManager);
        });

        sendNotificationService.sendAgitNotification(notificationType, task, message, null);
    }
}
