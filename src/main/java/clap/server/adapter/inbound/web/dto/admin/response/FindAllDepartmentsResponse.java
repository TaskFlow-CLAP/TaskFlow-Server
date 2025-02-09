package clap.server.adapter.inbound.web.dto.admin.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record FindAllDepartmentsResponse(
        Long departmentId,
        String name,
        @Schema(description = "담당자 권한 여부")
        Boolean isManager) {
}
