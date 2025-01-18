package clap.server.application.port.outbound.member;

import clap.server.domain.model.member.Department;

import java.util.Optional;

public interface CommandDepartmentPort {
    Optional<Department> findById(Long id);
}