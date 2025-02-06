package clap.server.application.port.outbound.task;

import clap.server.domain.model.task.Comment;

public interface CommandCommentPort {

    Comment saveComment(Comment comment);

    void deleteCommentWithTaskHistory(Long commentId);
}
