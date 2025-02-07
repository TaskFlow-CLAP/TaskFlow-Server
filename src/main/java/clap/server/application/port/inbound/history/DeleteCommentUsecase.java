package clap.server.application.port.inbound.history;

public interface DeleteCommentUsecase {

    void deleteComment(Long memberId, Long commentId);
}
