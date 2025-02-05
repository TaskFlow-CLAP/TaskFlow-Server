package clap.server.application.service.admin;

import clap.server.application.port.inbound.admin.FindAllDepartmentsUsecase;
import clap.server.application.port.outbound.member.LoadDepartmentPort;
import clap.server.common.annotation.architecture.ApplicationService;
import clap.server.domain.model.member.Department;
import lombok.RequiredArgsConstructor;

import java.util.List;

@ApplicationService
@RequiredArgsConstructor
public class FindAllDepartmentsService implements FindAllDepartmentsUsecase {
    private final LoadDepartmentPort loadDepartmentPort;

    @Override
    public List<Department> findAllDepartments() {
        return loadDepartmentPort.findActiveDepartments();
    }

}
