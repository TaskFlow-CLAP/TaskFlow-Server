package clap.server.adapter.inbound.web.admin;

import clap.server.adapter.inbound.web.dto.admin.AnonymousLogResponse;
import clap.server.adapter.inbound.web.dto.admin.MemberLogResponse;
import clap.server.application.port.inbound.log.GetApiLogsUseCase;
import clap.server.common.annotation.architecture.WebAdapter;
import clap.server.config.annotation.LogType;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@WebAdapter
@RestController
@RequestMapping("/api/management")
@RequiredArgsConstructor
public class LogController {

    private final GetApiLogsUseCase getApiLogsUseCase;

    @LogType("로그인 로그")
    @GetMapping("/login-logs")
    public List<AnonymousLogResponse> getLoginAttempts() {
        return getApiLogsUseCase.getAnonymousLogs();
    }

    @LogType("일반 로그")
    @GetMapping("/api-logs")
    public List<MemberLogResponse> getApiCalls(@RequestParam(defaultValue = "") String logType) {
        return getApiLogsUseCase.getMemberLogs();
    }
}
