package clap.server.application.service.admin;

import clap.server.adapter.inbound.web.dto.admin.response.FindAllDepartmentsResponse;
import clap.server.application.mapper.response.DepartmentResponseMapper;
import clap.server.application.port.inbound.admin.FindAllDepartmentsUsecase;
import clap.server.application.port.outbound.member.LoadDepartmentPort;
import clap.server.common.annotation.architecture.ApplicationService;
import lombok.RequiredArgsConstructor;

import java.util.List;

@ApplicationService
@RequiredArgsConstructor
public class FindAllDepartmentsService implements FindAllDepartmentsUsecase {
    private final LoadDepartmentPort loadDepartmentPort;

    @Override
    public List<FindAllDepartmentsResponse> findAllDepartments() {
        return loadDepartmentPort.findActiveDepartments().stream()
                .map(DepartmentResponseMapper::toFindAllDepartmentsResponse)
                .toList();
    }

}
