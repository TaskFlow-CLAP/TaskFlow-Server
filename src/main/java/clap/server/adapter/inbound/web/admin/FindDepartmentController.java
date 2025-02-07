package clap.server.adapter.inbound.web.admin;

import clap.server.adapter.inbound.web.dto.admin.response.FindAllDepartmentsResponse;
import clap.server.application.port.inbound.admin.FindAllDepartmentsUsecase;
import clap.server.common.annotation.architecture.WebAdapter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Tag(name = "05. Admin [회원 관리]")
@WebAdapter
@RequiredArgsConstructor
@RequestMapping("/api/managements")
public class FindDepartmentController {
    private final FindAllDepartmentsUsecase findAllDepartmentsUsecase;

    @Operation(summary = "부서 조회 API")
    @Secured("ROLE_ADMIN")
    @GetMapping("/departments")
    public ResponseEntity<List<FindAllDepartmentsResponse>> findAllDepartments() {
        return ResponseEntity.ok(findAllDepartmentsUsecase.findAllDepartments());
    }
}
