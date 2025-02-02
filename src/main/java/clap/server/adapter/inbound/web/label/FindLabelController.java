package clap.server.adapter.inbound.web.label;

import clap.server.adapter.inbound.security.SecurityUserDetails;
import clap.server.adapter.inbound.web.dto.common.SliceResponse;
import clap.server.adapter.inbound.web.dto.label.FindLabelListResponse;
import clap.server.application.port.inbound.label.FindLabelListUsecase;
import clap.server.common.annotation.architecture.WebAdapter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "02. Task [검토자]")
@WebAdapter
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/labels")
public class FindLabelController {

    private final FindLabelListUsecase findLabelListUsecase;

    @Operation(summary = "구분 목록 조회 API")
    @Parameters({
            @Parameter(name = "page", description = "조회할 목록 페이지 번호(0부터 시작)", example = "0", required = false),
            @Parameter(name = "size", description = "조회할 목록 페이지 당 개수", example = "5", required = false)
    })
    @Secured({"ROLE_MANAGER"})
    @GetMapping
    public ResponseEntity<SliceResponse<FindLabelListResponse>> findLabelList(
            @AuthenticationPrincipal SecurityUserDetails userInfo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(findLabelListUsecase.findLabelList(userInfo.getUserId(), pageable));
    }
}
