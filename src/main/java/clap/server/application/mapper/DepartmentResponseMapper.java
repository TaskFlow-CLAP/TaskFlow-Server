package clap.server.application.mapper;

import clap.server.adapter.inbound.web.dto.admin.response.FindAllDepartmentsResponse;
import clap.server.domain.model.member.Department;

public class DepartmentResponseMapper {
    public static FindAllDepartmentsResponse toFindAllDepartmentsResponse(Department department) {
        return new FindAllDepartmentsResponse(department.getDepartmentId(), department.getName());
    }
}
