package clap.server.adapter.outbound.persistense.repository.task;

import clap.server.adapter.outbound.persistense.entity.task.StatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatusRepository extends JpaRepository<StatusEntity, Long> {
}