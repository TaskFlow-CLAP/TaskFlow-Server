package clap.server.adapter.inbound.web.log;

import clap.server.adapter.inbound.web.dto.log.AnonymousLogResponse;
import clap.server.adapter.inbound.web.dto.log.MemberLogResponse;
import clap.server.application.port.inbound.log.FindApiLogsUsecase;
import clap.server.common.annotation.architecture.WebAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@WebAdapter
@RestController
@RequestMapping("/api/logs")
@RequiredArgsConstructor
public class LogController {

    private final FindApiLogsUsecase getApiLogsUseCase;

    @Secured({"ROLE_ADMIN"})
    @GetMapping("/login")
    public List<AnonymousLogResponse> getLoginAttempts() {
        return getApiLogsUseCase.getAnonymousLogs();
    }

    @Secured({"ROLE_ADMIN"})
    @GetMapping("/general")
    public List<MemberLogResponse> getApiCalls() {
        return getApiLogsUseCase.getMemberLogs();
    }
}
