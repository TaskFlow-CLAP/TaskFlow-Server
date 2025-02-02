package clap.server.application.service.task;

import clap.server.adapter.inbound.web.dto.task.request.ApprovalTaskRequest;
import clap.server.adapter.inbound.web.dto.task.response.ApprovalTaskResponse;
import clap.server.adapter.inbound.web.dto.task.response.FindApprovalFormResponse;
import clap.server.adapter.outbound.persistense.entity.notification.constant.NotificationType;
import clap.server.adapter.outbound.persistense.entity.task.constant.TaskHistoryType;
import clap.server.application.mapper.TaskMapper;
import clap.server.application.port.inbound.domain.CategoryService;
import clap.server.application.port.inbound.domain.LabelService;
import clap.server.application.port.inbound.domain.MemberService;
import clap.server.application.port.inbound.domain.TaskService;
import clap.server.application.port.inbound.task.ApprovalTaskUsecase;
import clap.server.application.port.outbound.task.CommandTaskPort;
import clap.server.application.port.outbound.taskhistory.CommandTaskHistoryPort;
import clap.server.application.service.webhook.SendPushNotificationService;
import clap.server.common.annotation.architecture.ApplicationService;
import clap.server.domain.model.member.Member;
import clap.server.domain.model.task.Category;
import clap.server.domain.model.task.Label;
import clap.server.domain.model.task.Task;
import clap.server.domain.model.task.TaskHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@ApplicationService
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ApprovalTaskService implements ApprovalTaskUsecase {

    private final MemberService memberService;
    private final TaskService taskService;
    private final CategoryService categoryService;
    private final LabelService labelService;
    private final CommandTaskPort commandTaskPort;
    private final CommandTaskHistoryPort commandTaskHistoryPort;
    private final SendPushNotificationService sendPushNotificationService;

    @Override
    @Transactional
    public ApprovalTaskResponse approvalTaskByReviewer(Long reviewerId, Long taskId, ApprovalTaskRequest approvalTaskRequest) {
        Member reviewer = memberService.findReviewer(reviewerId);
        Task task = taskService.findById(taskId);
        Member processor = memberService.findById(approvalTaskRequest.processorId());
        Category category = categoryService.findById(approvalTaskRequest.categoryId());
        Label label = labelService.findById(approvalTaskRequest.labelId());

        task.approveTask(reviewer, processor, approvalTaskRequest.dueDate(), category, label);
        TaskHistory taskHistory = TaskHistory.createTaskHistory(TaskHistoryType.PROCESSOR_ASSIGNED, task, null, processor,null);
        commandTaskHistoryPort.save(taskHistory);

        List<Member> receivers = new ArrayList<>();
        receivers.add(task.getRequester());
        receivers.add(task.getProcessor());

        publishNotification(receivers, task);
        return TaskMapper.toApprovalTaskResponse(commandTaskPort.save(task));
    }

    @Override
    public FindApprovalFormResponse findApprovalForm(Long managerId, Long taskId) {
        memberService.findActiveMember(managerId);
        Task task = taskService.findById(taskId);
        task.validateTaskRequested();
        return TaskMapper.toFindApprovalFormResponse(task);
    }

    private void publishNotification(List<Member> receivers, Task task){
        for (Member receiver : receivers) {

            sendPushNotificationService.sendPushNotification(receiver, NotificationType.PROCESSOR_ASSIGNED,
                    task, task.getProcessor().getNickname(), null);
        }
    }
}
