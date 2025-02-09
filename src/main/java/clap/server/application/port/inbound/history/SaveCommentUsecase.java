package clap.server.application.port.inbound.history;

import clap.server.adapter.inbound.web.dto.history.request.CreateCommentRequest;

public interface SaveCommentUsecase {

    void save(Long memberId, Long taskId, CreateCommentRequest request);
}
