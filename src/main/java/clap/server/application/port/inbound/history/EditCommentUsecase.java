package clap.server.application.port.inbound.history;

import clap.server.adapter.inbound.web.dto.history.request.EditCommentRequest;

@Deprecated
public interface EditCommentUsecase {

    @Deprecated
    void editComment(Long memberId, Long commentId, EditCommentRequest request);
}
