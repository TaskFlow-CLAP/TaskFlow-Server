package clap.server.application.port.inbound.task;

import clap.server.adapter.inbound.web.dto.task.request.ApprovalTaskRequest;
import clap.server.adapter.inbound.web.dto.task.response.ApprovalTaskResponse;
import clap.server.adapter.inbound.web.dto.task.response.FindApprovalFormResponse;

public interface ApprovalTaskUsecase {
    ApprovalTaskResponse approvalTaskByReviewer(Long userId, Long taskId, ApprovalTaskRequest approvalTaskRequest);
    FindApprovalFormResponse findApprovalForm(Long managerId, Long taskId);
}
