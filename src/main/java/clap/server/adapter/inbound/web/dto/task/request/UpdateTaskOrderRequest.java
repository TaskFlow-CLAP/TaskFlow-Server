package clap.server.adapter.inbound.web.dto.task.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;

public record UpdateTaskOrderRequest(
        @Schema(description = "변경할 위치의 상위 작업 ID, 가장 상위일 경우 0 입력")
        long prevTaskId,
        @Min(1) @Schema(description = "순서 또는 상태를 변경할 작업의 ID")
        long targetTaskId,
        @Schema(description = "변경할 위치의 하위 작업 ID, 가장 하위일 경우 0 입력")
        long nextTaskId
) {
}
