package clap.server.application.port.inbound.comment;

import clap.server.adapter.inbound.web.dto.comment.CreateCommentRequest;

public interface SaveCommentUsecase {

    void save(Long userId, Long taskId, CreateCommentRequest request);
}
