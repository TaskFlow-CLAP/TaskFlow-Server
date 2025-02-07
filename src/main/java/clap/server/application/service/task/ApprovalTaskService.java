package clap.server.application.service.task;

import clap.server.adapter.inbound.web.dto.task.request.ApprovalTaskRequest;
import clap.server.adapter.inbound.web.dto.task.response.ApprovalTaskResponse;
import clap.server.adapter.inbound.web.dto.task.response.FindApprovalFormResponse;
import clap.server.adapter.outbound.persistense.entity.member.constant.MemberRole;
import clap.server.adapter.outbound.persistense.entity.notification.constant.NotificationType;
import clap.server.adapter.outbound.persistense.entity.task.constant.TaskHistoryType;
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

import java.util.List;

@ApplicationService
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ApprovalTaskService implements ApprovalTaskUsecase {

    private final MemberService memberService;
    private final TaskService taskService;
    private final CategoryService categoryService;
    private final LabelService labelService;
    private final RequestedTaskUpdatePolicy requestedTaskUpdatePolicy;
    private final CommandTaskHistoryPort commandTaskHistoryPort;
    private final SendNotificationService sendNotificationService;

    @Override
    @Transactional
    public ApprovalTaskResponse approvalTaskByReviewer(Long reviewerId, Long taskId, ApprovalTaskRequest approvalTaskRequest) {
        Member reviewer = memberService.findReviewer(reviewerId);
        Task task = taskService.findById(taskId);
        Member processor = memberService.findById(approvalTaskRequest.processorId());
        Category category = categoryService.findById(approvalTaskRequest.categoryId());
        Label label = null;
        if (approvalTaskRequest.labelId() != null) {
            label = labelService.findById(approvalTaskRequest.labelId());
        }

        requestedTaskUpdatePolicy.validateTaskRequested(task);
        task.approveTask(reviewer, processor, approvalTaskRequest.dueDate(), category, label);
        TaskHistory taskHistory = TaskHistory.createTaskHistory(TaskHistoryType.PROCESSOR_ASSIGNED, task, null, processor,null);
        commandTaskHistoryPort.save(taskHistory);

        List<Member> receivers = List.of(task.getRequester(), processor);
        String processorName = processor.getNickname();
        publishNotification(receivers, task, processorName);

        return TaskResponseMapper.toApprovalTaskResponse(taskService.upsert(task));
    }

    @Override
    public FindApprovalFormResponse findApprovalForm(Long managerId, Long taskId) {
        memberService.findActiveMember(managerId);
        Task task = taskService.findById(taskId);
        requestedTaskUpdatePolicy.validateTaskRequested(task);
        return TaskResponseMapper.toFindApprovalFormResponse(task);
    }

    private void publishNotification(List<Member> receivers, Task task, String processorName){
        receivers.forEach(receiver -> {
            boolean isManager = receiver.getMemberInfo().getRole() == MemberRole.ROLE_MANAGER;
            sendNotificationService.sendPushNotification(receiver, NotificationType.PROCESSOR_ASSIGNED,
                    task, processorName, null, isManager);
        });
        sendNotificationService.sendAgitNotification(NotificationType.PROCESSOR_ASSIGNED,
                task, processorName, null);
    }

}
