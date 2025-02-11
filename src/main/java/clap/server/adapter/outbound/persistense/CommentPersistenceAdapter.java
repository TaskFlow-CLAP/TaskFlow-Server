package clap.server.adapter.outbound.persistense;

import clap.server.adapter.outbound.persistense.entity.task.CommentEntity;
import clap.server.adapter.outbound.persistense.mapper.CommentPersistenceMapper;
import clap.server.adapter.outbound.persistense.repository.history.CommentRepository;
import clap.server.application.port.outbound.task.CommandCommentPort;
import clap.server.application.port.outbound.task.LoadCommentPort;
import clap.server.common.annotation.architecture.PersistenceAdapter;
import clap.server.domain.model.task.Comment;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@PersistenceAdapter
@RequiredArgsConstructor
public class CommentPersistenceAdapter implements LoadCommentPort, CommandCommentPort {

    private final CommentRepository commentRepository;
    private final CommentPersistenceMapper commentPersistenceMapper;

    @Override
    public Optional<Comment> findById(final Long commentId) {
        Optional<CommentEntity> commentEntity = commentRepository.findById(commentId);
        return commentEntity.map(commentPersistenceMapper::toDomain);
    }

    @Override
    public Comment saveComment(final Comment comment) {
        CommentEntity commentEntity = commentRepository.save(commentPersistenceMapper.toEntity(comment));
        return commentPersistenceMapper.toDomain(commentEntity);
    }

    @Override
    public void deleteCommentWithTaskHistory(final Long commentId) {
        commentRepository.deleteCommentWithTaskHistory(commentId);
    }
}
