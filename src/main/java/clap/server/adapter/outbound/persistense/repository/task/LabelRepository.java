package clap.server.adapter.outbound.persistense.repository.task;

import clap.server.adapter.outbound.persistense.entity.task.LabelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LabelRepository extends JpaRepository<LabelEntity, Long> {

    List<LabelEntity> findByIsDeletedFalse();

    boolean existsByLabelNameAndIsDeletedFalse(String labelName);
}
