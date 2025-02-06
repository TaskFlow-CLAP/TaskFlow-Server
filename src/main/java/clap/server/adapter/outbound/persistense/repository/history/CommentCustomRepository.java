package clap.server.adapter.outbound.persistense.repository.history;


public interface CommentCustomRepository {
    void deleteCommentWithTaskHistory(Long commentId);
}
