package clap.server.adapter.outbound.persistense.repository.member;

import clap.server.adapter.outbound.persistense.entity.member.DepartmentEntity;
import clap.server.adapter.outbound.persistense.entity.member.constant.DepartmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartmentRepository extends JpaRepository<DepartmentEntity, Long> {
    List<DepartmentEntity> findAllByStatusIs(DepartmentStatus status);
}