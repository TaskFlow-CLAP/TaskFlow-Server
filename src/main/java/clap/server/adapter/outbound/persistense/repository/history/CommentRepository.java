package clap.server.adapter.outbound.persistense.repository.history;

import clap.server.adapter.outbound.persistense.entity.task.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long>, CommentCustomRepository {
}