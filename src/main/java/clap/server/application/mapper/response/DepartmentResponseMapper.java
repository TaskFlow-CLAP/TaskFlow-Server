package clap.server.application.mapper.response;

import clap.server.adapter.inbound.web.dto.admin.response.FindAllDepartmentsResponse;
import clap.server.domain.model.member.Department;

public class DepartmentResponseMapper {
    private DepartmentResponseMapper() {
        throw new IllegalStateException("Utility class");
    }

    public static FindAllDepartmentsResponse toFindAllDepartmentsResponse(Department department) {
        return new FindAllDepartmentsResponse(
                department.getDepartmentId(),
                department.getName(),
                department.isManager());
    }
}
