package clap.server.adapter.outbound.persistense;

import clap.server.application.port.outbound.task.LoadCommentPort;
import clap.server.domain.model.task.Comment;

import java.util.Optional;

public class CommentPersistenceAdapter implements LoadCommentPort {
    @Override
    public Optional<Comment> findById(Long id) {
        return Optional.empty();
    }
}
