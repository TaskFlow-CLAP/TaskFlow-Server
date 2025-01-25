package clap.server.application.port.inbound.task;

import clap.server.adapter.inbound.web.dto.task.ApprovalTaskRequest;
import clap.server.adapter.inbound.web.dto.task.ApprovalTaskResponse;

public interface ApprovalTaskUsecase {
    ApprovalTaskResponse approvalTaskByReviewer(Long userId, ApprovalTaskRequest approvalTaskRequest);
}
