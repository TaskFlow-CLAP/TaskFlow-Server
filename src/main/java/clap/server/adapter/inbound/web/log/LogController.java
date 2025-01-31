package clap.server.adapter.inbound.web.log;

import clap.server.adapter.inbound.security.SecurityUserDetails;
import clap.server.adapter.inbound.web.dto.log.AnonymousLogResponse;
import clap.server.adapter.inbound.web.dto.log.MemberLogRequest;
import clap.server.adapter.inbound.web.dto.log.MemberLogResponse;
import clap.server.adapter.inbound.web.dto.task.FilterTaskListRequest;
import clap.server.application.port.inbound.log.FindApiLogsUsecase;
import clap.server.common.annotation.architecture.WebAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@WebAdapter
@RestController
@RequestMapping("/api/logs")
@RequiredArgsConstructor
public class LogController {

    private final FindApiLogsUsecase findApiLogsUsecase;

    @Secured({"ROLE_ADMIN"})
    @GetMapping("/login")
    public List<AnonymousLogResponse> getLoginAttempts() {
        return findApiLogsUsecase.getAnonymousLogs();
    }

    @Secured({"ROLE_ADMIN"})
    @GetMapping("/general")
    public Page<MemberLogResponse> getApiCalls(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int pageSize,
            @ModelAttribute MemberLogRequest memberLogRequest,
            @AuthenticationPrincipal SecurityUserDetails userInfo) {
        Pageable pageable = PageRequest.of(page, pageSize);
        return findApiLogsUsecase.filterMemberLogs(memberLogRequest, pageable);
    }
}
