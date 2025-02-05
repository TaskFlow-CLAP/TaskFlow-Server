package clap.server.adapter.inbound.web.task;

import clap.server.adapter.inbound.security.service.SecurityUserDetails;
import clap.server.adapter.inbound.web.dto.task.request.TerminateTaskRequest;
import clap.server.application.port.inbound.task.TerminateTaskUsecase;
import clap.server.common.annotation.architecture.WebAdapter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "02. Task [거부 & 종료]")
@WebAdapter
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tasks")
public class TerminateTaskController {
    private final TerminateTaskUsecase terminateTaskUsecase;

    @Operation(summary = "작업 거부 및 종료")
    @Secured({"ROLE_MANAGER"})
    @PatchMapping("/{taskId}/terminate")
    public void terminateTask(@AuthenticationPrincipal SecurityUserDetails userInfo,
                              @PathVariable Long taskId,
                              @RequestBody TerminateTaskRequest request) {
        terminateTaskUsecase.terminateTask(userInfo.getUserId(), taskId, request.reason());
    }

}
