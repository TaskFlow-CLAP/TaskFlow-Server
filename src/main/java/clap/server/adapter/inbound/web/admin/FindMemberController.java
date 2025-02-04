package clap.server.adapter.inbound.web.admin;

import clap.server.adapter.inbound.web.dto.admin.request.FindMemberRequest;
import clap.server.adapter.inbound.web.dto.admin.response.RetrieveAllMemberResponse;
import clap.server.adapter.inbound.web.dto.common.PageResponse;
import clap.server.application.port.inbound.admin.FindMembersWithFilterUsecase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/managements")
@RequiredArgsConstructor
public class FindMemberController {
    private final FindMembersWithFilterUsecase findMembersWithFilterUsecase;

    @Tag(name = "05. Admin")
    @Secured({"ROLE_ADMIN"})
    @Operation(
            summary = "전체 회원 조회 API",
            description = "모든 회원 정보를 페이징 처리하여 반환하거나 조건에 맞는 회원 정보를 반환합니다.",
            parameters = {
                    @Parameter(name = "page", description = "조회할 페이지 번호 (0부터 시작, 기본값: 0)", example = "0"),
                    @Parameter(name = "pageSize", description = "페이지 당 회원 수 (기본값: 20)", example = "20"),
                    @Parameter(name = "sortDirection", description = "정렬 방향 (ASC 또는 DESC, 기본값: DESC)", example = "DESC")

            }
    )
    @GetMapping("/members")
    public ResponseEntity<PageResponse<RetrieveAllMemberResponse>> getAllMembers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int pageSize,
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @ModelAttribute @Valid FindMemberRequest filterRequest) {

        Pageable pageable = PageRequest.of(page, pageSize);
        PageResponse<RetrieveAllMemberResponse> response = findMembersWithFilterUsecase.findMembersWithFilter(pageable, filterRequest, sortDirection);
        return ResponseEntity.ok(response);
    }
}