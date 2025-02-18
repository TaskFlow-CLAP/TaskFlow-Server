package clap.server.adapter.outbound.persistense.mapper;

import clap.server.adapter.outbound.persistense.entity.member.DepartmentEntity;
import clap.server.adapter.outbound.persistense.mapper.common.PersistenceMapper;
import clap.server.domain.model.member.Department;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DepartmentPersistenceMapper extends PersistenceMapper<DepartmentEntity, Department> {

    @Mapping(target = "adminId", ignore = true)
    @Mapping(source = "manager", target = "isManager")
    Department toDomain(DepartmentEntity entity);

    @Mapping(source = "adminId", target = "admin.memberId")
    @Mapping(source = "manager", target = "isManager")
    DepartmentEntity toEntity(Department domain);
}