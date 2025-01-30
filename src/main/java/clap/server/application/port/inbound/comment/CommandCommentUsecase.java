package clap.server.application.port.inbound.comment;

import clap.server.adapter.inbound.web.dto.task.DeleteCommentRequest;
import clap.server.adapter.inbound.web.dto.task.PostAndEditCommentRequest;

import java.util.List;

public interface CommandCommentUsecase {

    void updateComment(Long userId, Long commentId, PostAndEditCommentRequest request);

    void deleteComment(Long userId, Long commentId, DeleteCommentRequest request);
}
