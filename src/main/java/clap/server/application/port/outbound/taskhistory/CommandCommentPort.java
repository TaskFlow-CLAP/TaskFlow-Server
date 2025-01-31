package clap.server.application.port.outbound.taskhistory;

import clap.server.domain.model.task.Comment;

import java.util.Optional;

public interface CommandCommentPort {
    Optional<Comment> save(Comment comment);
}