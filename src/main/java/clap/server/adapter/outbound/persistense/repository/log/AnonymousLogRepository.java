package clap.server.adapter.outbound.persistense.repository.log;

import aj.org.objectweb.asm.commons.Remapper;
import clap.server.adapter.inbound.web.dto.log.FilterLogRequest;
import clap.server.adapter.outbound.persistense.entity.log.AnonymousLogEntity;
import clap.server.domain.model.log.AnonymousLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnonymousLogRepository extends JpaRepository<AnonymousLogEntity, Long>, AnonymousLogCustomRepository{
}
