package clap.server.adapter.outbound.persistense.repository.history;

import clap.server.adapter.outbound.persistense.entity.task.TaskHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskHistoryRepository extends JpaRepository<TaskHistoryEntity, Long> , TaskHistoryCustomRepository{
}