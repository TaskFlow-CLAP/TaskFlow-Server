package clap.server.adapter.outbound.persistense.mapper;

import clap.server.adapter.outbound.persistense.entity.member.DepartmentEntity;
import clap.server.adapter.outbound.persistense.mapper.common.PersistenceMapper;
import clap.server.domain.model.member.Department;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DepartmentPersistenceMapper extends PersistenceMapper<DepartmentEntity, Department> {
}
