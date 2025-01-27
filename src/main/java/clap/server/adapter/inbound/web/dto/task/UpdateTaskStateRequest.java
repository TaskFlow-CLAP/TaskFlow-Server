package clap.server.adapter.inbound.web.dto.task;

import clap.server.adapter.outbound.persistense.entity.task.constant.TaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "작업 상태 변경 요청")
public record UpdateTaskStateRequest(

        @Schema(description = "변경하고 싶은 작업 상태", example = "완료")
        @NotNull
        TaskStatus taskStatus
) {
}
