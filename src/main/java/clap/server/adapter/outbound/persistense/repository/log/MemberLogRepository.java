package clap.server.adapter.outbound.persistense.repository.log;

import clap.server.adapter.outbound.persistense.entity.log.MemberLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberLogRepository extends JpaRepository<MemberLogEntity, Long> {
}