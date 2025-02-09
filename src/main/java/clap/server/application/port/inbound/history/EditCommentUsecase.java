package clap.server.application.port.inbound.history;

import clap.server.adapter.inbound.web.dto.history.request.EditCommentRequest;

public interface EditCommentUsecase {

    void editComment(Long memberId, Long commentId, EditCommentRequest request);
}
