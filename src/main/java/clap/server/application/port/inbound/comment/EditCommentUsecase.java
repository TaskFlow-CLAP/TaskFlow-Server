package clap.server.application.port.inbound.comment;

import clap.server.adapter.inbound.web.dto.comment.EditCommentRequest;

public interface EditCommentUsecase {

    void editComment(Long userId, Long commentId, EditCommentRequest request);
}
