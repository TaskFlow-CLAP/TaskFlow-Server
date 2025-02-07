package clap.server.adapter.inbound.web.log;

import clap.server.adapter.inbound.security.service.SecurityUserDetails;
import clap.server.adapter.inbound.web.dto.common.PageResponse;
import clap.server.adapter.inbound.web.dto.log.response.AnonymousLogResponse;
import clap.server.adapter.inbound.web.dto.log.request.FilterLogRequest;
import clap.server.adapter.inbound.web.dto.log.response.MemberLogResponse;
import clap.server.application.port.inbound.log.FindApiLogsUsecase;
import clap.server.common.annotation.architecture.WebAdapter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "05. Admin [로깅]")
@WebAdapter
@RequestMapping("/api/managements/logs")
@RequiredArgsConstructor
public class LogController {

    private final FindApiLogsUsecase findApiLogsUsecase;

    @Operation(summary = "로그인 로그 목록 조회")
    @Secured({"ROLE_ADMIN"})
    @GetMapping("/login")
    public ResponseEntity<PageResponse<AnonymousLogResponse>> getLoginAttempts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int pageSize,
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @ModelAttribute @Valid FilterLogRequest anonymousLogRequest,
            @AuthenticationPrincipal SecurityUserDetails userInfo) {
        Pageable pageable = PageRequest.of(page, pageSize);
        return ResponseEntity.ok(findApiLogsUsecase.filterAnonymousLogs(anonymousLogRequest, pageable, sortDirection));
    }

    @Operation(summary = "작업 로그 목록 조회")
    @Secured({"ROLE_ADMIN"})
    @GetMapping("/general")
    public ResponseEntity<PageResponse<MemberLogResponse>> getApiCalls(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int pageSize,
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @ModelAttribute @Valid FilterLogRequest memberLogRequest,
            @AuthenticationPrincipal SecurityUserDetails userInfo) {
        Pageable pageable = PageRequest.of(page, pageSize);
        return ResponseEntity.ok(findApiLogsUsecase.filterMemberLogs(memberLogRequest, pageable, sortDirection));
    }
}
