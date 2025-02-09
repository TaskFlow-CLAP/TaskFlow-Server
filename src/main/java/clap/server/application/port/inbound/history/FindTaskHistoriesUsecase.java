package clap.server.application.port.inbound.history;

import clap.server.adapter.inbound.web.dto.history.response.FindTaskHistoryResponse;

public interface FindTaskHistoriesUsecase {
    FindTaskHistoryResponse findTaskHistories(Long memberId, Long taskId);
}
