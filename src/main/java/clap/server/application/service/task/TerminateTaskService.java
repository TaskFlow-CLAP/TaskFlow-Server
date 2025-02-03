package clap.server.application.service.task;

import clap.server.adapter.outbound.persistense.entity.task.constant.TaskHistoryType;
import clap.server.application.port.inbound.domain.MemberService;
import clap.server.application.port.inbound.domain.TaskService;
import clap.server.application.port.inbound.task.TerminateTaskUsecase;
import clap.server.application.port.outbound.taskhistory.CommandTaskHistoryPort;
import clap.server.common.annotation.architecture.ApplicationService;
import clap.server.domain.model.task.Task;
import clap.server.domain.model.task.TaskHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@ApplicationService
@RequiredArgsConstructor
@Transactional
public class TerminateTaskService implements TerminateTaskUsecase {
    private final MemberService memberService;
    private final TaskService taskService;
    private final CommandTaskHistoryPort commandTaskHistoryPort;

    @Override
    public void terminateTask(Long memberId, Long taskId, String reason) {
        memberService.findReviewer(memberId);
        Task task = taskService.findById(taskId);
        task.terminateTask();
        taskService.upsert(task);

        TaskHistory taskHistory = TaskHistory.createTaskHistory(TaskHistoryType.TASK_TERMINATED, task, reason, null, null);
        commandTaskHistoryPort.save(taskHistory);
    }
}
