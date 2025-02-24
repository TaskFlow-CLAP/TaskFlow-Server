package clap.server.application.service.task;

import clap.server.adapter.inbound.web.dto.task.request.ApprovalTaskRequest;
import clap.server.adapter.inbound.web.dto.task.response.ApprovalTaskResponse;
import clap.server.adapter.inbound.web.dto.task.response.FindApprovalFormResponse;
import clap.server.adapter.outbound.persistense.entity.member.constant.MemberRole;
import clap.server.adapter.outbound.persistense.entity.notification.constant.NotificationType;
import clap.server.adapter.outbound.persistense.entity.task.constant.TaskHistoryType;
import clap.server.adapter.outbound.persistense.entity.task.constant.TaskStatus;
import clap.server.application.mapper.response.TaskResponseMapper;
import clap.server.application.port.inbound.domain.CategoryService;
import clap.server.application.port.inbound.domain.LabelService;
import clap.server.application.port.inbound.domain.MemberService;
import clap.server.application.port.inbound.domain.TaskService;
import clap.server.application.port.inbound.task.ApprovalTaskUsecase;
import clap.server.application.port.outbound.taskhistory.CommandTaskHistoryPort;
import clap.server.application.service.webhook.SendNotificationService;
import clap.server.common.annotation.architecture.ApplicationService;
import clap.server.domain.model.member.Member;
import clap.server.domain.model.task.Category;
import clap.server.domain.model.task.Label;
import clap.server.domain.model.task.Task;
import clap.server.domain.model.task.TaskHistory;
import clap.server.domain.policy.task.RequestedTaskUpdatePolicy;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ApplicationService
@RequiredArgsConstructor
public class ApprovalTaskService implements ApprovalTaskUsecase {
    private final MemberService memberService;
    private final TaskService taskService;
    private final CategoryService categoryService;
    private final LabelService labelService;

    private final RequestedTaskUpdatePolicy requestedTaskUpdatePolicy;
    private final CommandTaskHistoryPort commandTaskHistoryPort;
    private final SendNotificationService sendNotificationService;
    private final UpdateProcessorTaskCountService updateProcessorTaskCountService;

    @Override
    @Transactional
    public ApprovalTaskResponse approvalTaskByReviewer(Long reviewerId, Long taskId, ApprovalTaskRequest approvalTaskRequest) {
        Member reviewer = memberService.findReviewer(reviewerId);
        Task task = taskService.findById(taskId);
        Member processor = memberService.findActiveMemberWithDepartment(approvalTaskRequest.processorId());
        Category category = categoryService.findById(approvalTaskRequest.categoryId());
        Label label = null;
        if (approvalTaskRequest.labelId() != null) {
            label = labelService.findById(approvalTaskRequest.labelId());
        }

        requestedTaskUpdatePolicy.validateTaskRequested(task);
        updateProcessorTaskCountService.handleTaskStatusChange(processor, TaskStatus.REQUESTED, TaskStatus.IN_PROGRESS);
        task.approveTask(reviewer, processor, approvalTaskRequest.dueDate(), category, label);

        TaskHistory taskHistory = TaskHistory.createTaskHistory(TaskHistoryType.PROCESSOR_ASSIGNED, task, null, processor);
        commandTaskHistoryPort.save(taskHistory);

        String processorName = processor.getNickname();
        publishNotification(task, processorName);

        return TaskResponseMapper.toApprovalTaskResponse(taskService.upsert(task));
    }

    @Override
    public FindApprovalFormResponse findApprovalForm(Long managerId, Long taskId) {
        memberService.findActiveMember(managerId);
        Task task = taskService.findById(taskId);
        requestedTaskUpdatePolicy.validateTaskRequested(task);
        return TaskResponseMapper.toFindApprovalFormResponse(task);
    }

    private void publishNotification(Task task, String processorName) {
        List<Member> receivers = new ArrayList<>();
        receivers.add(task.getProcessor());

        receivers.forEach(receiver -> {
            boolean isManager = receiver.getMemberInfo().getRole() == MemberRole.ROLE_MANAGER;
            sendNotificationService.sendPushNotification(receiver, NotificationType.PROCESSOR_ASSIGNED,
                    task, processorName, null, null, isManager);
        });
        sendNotificationService.sendAgitNotification(NotificationType.PROCESSOR_ASSIGNED,
                task, processorName, null);
    }

}
