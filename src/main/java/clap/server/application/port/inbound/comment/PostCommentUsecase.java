package clap.server.application.port.inbound.comment;

import clap.server.adapter.inbound.web.dto.task.PostAndEditCommentRequest;

public interface PostCommentUsecase {

    void save(Long userId, Long taskId, PostAndEditCommentRequest request);
}
