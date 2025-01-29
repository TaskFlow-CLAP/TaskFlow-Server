package clap.server.adapter.inbound.web.admin;

import clap.server.adapter.inbound.security.SecurityUserDetails;
import clap.server.adapter.inbound.web.dto.common.SliceResponse;
import clap.server.adapter.inbound.web.dto.label.FindLabelListResponse;
import clap.server.application.port.inbound.label.FindLabelListUsecase;
import clap.server.common.annotation.architecture.WebAdapter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Tag(name = "05. Admin")
@WebAdapter
@RequiredArgsConstructor
@RequestMapping("/api/managements/label")
public class FindLabelAdminController {

    private final FindLabelListUsecase findLabelListUsecase;

    @Operation(summary = "구분 목록 조회 API")
    @Secured({"ROLE_ADMIN"})
    @GetMapping
    public ResponseEntity<List<FindLabelListResponse>> findLabelList(
            @AuthenticationPrincipal SecurityUserDetails userInfo) {
        return ResponseEntity.ok(findLabelListUsecase.findLabelListAdmin(userInfo.getUserId()));
    }
}
