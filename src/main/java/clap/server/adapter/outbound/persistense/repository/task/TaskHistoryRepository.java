package clap.server.adapter.outbound.persistense.repository.task;

import clap.server.adapter.outbound.persistense.entity.task.TaskHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskHistoryRepository extends JpaRepository<TaskHistoryEntity, Long> , TaskHistoryCustomRepository{
    List<TaskHistoryEntity> findAllByTaskModificationInfo_Task_TaskId(Long taskId);
}