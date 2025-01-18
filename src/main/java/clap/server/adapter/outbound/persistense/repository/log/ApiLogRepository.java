package clap.server.adapter.outbound.persistense.repository.log;

import clap.server.adapter.outbound.persistense.entity.log.ApiLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiLogRepository extends JpaRepository<ApiLogEntity, Long> {
}