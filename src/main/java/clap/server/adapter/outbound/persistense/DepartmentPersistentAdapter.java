package clap.server.adapter.outbound.persistense;

import clap.server.adapter.outbound.persistense.entity.member.DepartmentEntity;
import clap.server.adapter.outbound.persistense.mapper.DepartmentPersistenceMapper;
import clap.server.adapter.outbound.persistense.repository.member.DepartmentRepository;
import clap.server.application.port.outbound.member.CommandDepartmentPort;
import clap.server.application.port.outbound.member.LoadDepartmentPort;
import clap.server.common.annotation.architecture.PersistenceAdapter;
import clap.server.domain.model.member.Department;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@PersistenceAdapter
@RequiredArgsConstructor
public class DepartmentPersistentAdapter implements LoadDepartmentPort, CommandDepartmentPort {
    private final DepartmentRepository departmentRepository;
    private final DepartmentPersistenceMapper departmentPersistenceMapper;

    @Override
    public Optional<Department> findById(final Long id) {
        Optional<DepartmentEntity> departmentEntity = departmentRepository.findById(id);
        return departmentEntity.map(departmentPersistenceMapper::toDomain);
    }
}
