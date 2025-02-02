package clap.server.application.port.inbound.task;

import clap.server.adapter.inbound.web.dto.task.response.FindTaskHistoryResponse;

public interface FindTaskHistoriesUsecase {
    FindTaskHistoryResponse findTaskHistories(Long userId, Long taskId);
}
