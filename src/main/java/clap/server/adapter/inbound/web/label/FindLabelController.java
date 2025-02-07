package clap.server.adapter.inbound.web.label;

import clap.server.adapter.inbound.security.service.SecurityUserDetails;
import clap.server.adapter.inbound.web.dto.label.response.FindLabelListResponse;
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
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "02. Task [조회]", description = "담당자 및 관리자 공통으로 사용")
@WebAdapter
@RequiredArgsConstructor
@RequestMapping("/api/labels")
public class FindLabelController {

    private final FindLabelListUsecase findLabelListUsecase;

    @Operation(summary = "구분 목록 조회 API")
    @Secured({"ROLE_MANAGER", "ROLE_ADMIN"})
    @GetMapping
    public ResponseEntity<List<FindLabelListResponse>> findLabelList(
            @AuthenticationPrincipal SecurityUserDetails userInfo) {
        return ResponseEntity.ok(findLabelListUsecase.findLabelList(userInfo.getUserId()));
    }
}
