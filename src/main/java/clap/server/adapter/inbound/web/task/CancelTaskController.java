package clap.server.adapter.inbound.web.task;

import clap.server.application.port.inbound.task.CancelTaskUsecase;
import clap.server.common.annotation.architecture.WebAdapter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "02. Task [거부 & 종료]")
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
@WebAdapter
public class CancelTaskController {
    private final CancelTaskUsecase cancelTaskUsecase;

    @Operation(summary = "작업 취소")
    @Secured("ROLE_USER")
    @PatchMapping("/{taskId}/cancle")
    public void cancelTask(@PathVariable Long taskId) {
        cancelTaskUsecase.cancleTask(taskId);
    }
}
