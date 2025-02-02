package clap.server.application.port.inbound.comment;

public interface DeleteCommentUsecase {

    void deleteComment(Long userId, Long commentId);
}
