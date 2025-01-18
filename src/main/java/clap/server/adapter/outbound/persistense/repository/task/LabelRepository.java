package clap.server.adapter.outbound.persistense.repository.task;

import clap.server.adapter.outbound.persistense.entity.task.LabelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LabelRepository extends JpaRepository<LabelEntity, Long> {
}
