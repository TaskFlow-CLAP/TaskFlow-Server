package clap.server.application.port.inbound.admin;

import clap.server.domain.model.member.Department;

import java.util.List;

public interface FindAllDepartmentsUsecase {
    List<Department> findAllDepartments();
}
