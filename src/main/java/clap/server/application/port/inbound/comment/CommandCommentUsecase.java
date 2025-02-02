package clap.server.application.port.inbound.comment;

import clap.server.adapter.inbound.web.dto.task.PostAndEditCommentRequest;

public interface CommandCommentUsecase {

    void updateComment(Long userId, Long commentId, PostAndEditCommentRequest request);

    void deleteComment(Long userId, Long commentId);
}
