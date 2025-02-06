package clap.server.application.port.inbound.admin;

import clap.server.adapter.inbound.web.dto.admin.response.FindAllDepartmentsResponse;

import java.util.List;

public interface FindAllDepartmentsUsecase {
    List<FindAllDepartmentsResponse> findAllDepartments();
}
