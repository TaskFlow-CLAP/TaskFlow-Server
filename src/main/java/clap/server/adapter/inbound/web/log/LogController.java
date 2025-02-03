package clap.server.adapter.inbound.web.log;

import clap.server.adapter.inbound.security.SecurityUserDetails;
import clap.server.adapter.inbound.web.dto.common.PageResponse;
import clap.server.adapter.inbound.web.dto.log.AnonymousLogResponse;
import clap.server.adapter.inbound.web.dto.log.FilterLogRequest;
import clap.server.adapter.inbound.web.dto.log.MemberLogResponse;
import clap.server.application.port.inbound.log.FindApiLogsUsecase;
import clap.server.common.annotation.architecture.WebAdapter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "05. Admin [로깅]")
@WebAdapter
@RestController
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
            @ModelAttribute @Valid FilterLogRequest anonymousLogRequest,
            @AuthenticationPrincipal SecurityUserDetails userInfo) {
        Pageable pageable = PageRequest.of(page, pageSize);
        return ResponseEntity.ok(findApiLogsUsecase.filterAnonymousLogs(anonymousLogRequest, pageable));
    }

    @Operation(summary = "작업 로그 목록 조회")
    @Secured({"ROLE_ADMIN"})
    @GetMapping("/general")
    public ResponseEntity<PageResponse<MemberLogResponse>> getApiCalls(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int pageSize,
            @ModelAttribute @Valid FilterLogRequest memberLogRequest,
            @AuthenticationPrincipal SecurityUserDetails userInfo) {
        Pageable pageable = PageRequest.of(page, pageSize);
        return ResponseEntity.ok(findApiLogsUsecase.filterMemberLogs(memberLogRequest, pageable));
    }
}
