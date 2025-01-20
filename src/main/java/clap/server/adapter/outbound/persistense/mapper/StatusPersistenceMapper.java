package clap.server.adapter.outbound.persistense.mapper;

import clap.server.adapter.outbound.persistense.entity.task.StatusEntity;
import clap.server.adapter.outbound.persistense.mapper.common.PersistenceMapper;
import clap.server.domain.model.task.Status;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {MemberPersistenceMapper.class})
public interface StatusPersistenceMapper extends PersistenceMapper<StatusEntity, Status> {
}
