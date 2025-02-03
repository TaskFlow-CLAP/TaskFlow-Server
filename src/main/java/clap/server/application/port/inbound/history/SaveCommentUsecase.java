package clap.server.application.port.inbound.history;

import clap.server.adapter.inbound.web.dto.history.CreateCommentRequest;

public interface SaveCommentUsecase {

    void save(Long userId, Long taskId, CreateCommentRequest request);
}
