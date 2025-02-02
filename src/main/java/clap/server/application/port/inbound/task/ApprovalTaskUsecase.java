package clap.server.application.port.inbound.task;

import clap.server.adapter.inbound.web.dto.task.ApprovalTaskRequest;
import clap.server.adapter.inbound.web.dto.task.ApprovalTaskResponse;
import clap.server.adapter.inbound.web.dto.task.FindApprovalFormResponse;

public interface ApprovalTaskUsecase {
    ApprovalTaskResponse approvalTaskByReviewer(Long userId, Long taskId, ApprovalTaskRequest approvalTaskRequest);
    FindApprovalFormResponse findApprovalForm(Long managerId, Long taskId);
}
