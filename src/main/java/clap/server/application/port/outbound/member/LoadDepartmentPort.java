package clap.server.application.port.outbound.member;

import clap.server.domain.model.member.Department;

import java.util.List;
import java.util.Optional;

public interface LoadDepartmentPort {
    Optional<Department> findById(Long id);
    List<Department> findActiveDepartments();
}