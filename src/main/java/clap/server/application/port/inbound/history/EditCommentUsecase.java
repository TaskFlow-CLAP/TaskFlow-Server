package clap.server.application.port.inbound.history;

import clap.server.adapter.inbound.web.dto.history.request.EditCommentRequest;

public interface EditCommentUsecase {

    void editComment(Long userId, Long commentId, EditCommentRequest request);
}
