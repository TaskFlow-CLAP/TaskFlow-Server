package clap.server.adapter.inbound.web.dto.task;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record DeleteCommentRequest(
        @Schema(description = "삭제할 파일 ID 목록, 없을 경우 emptylist 전송")
        @NotNull
        List<Long> attachmentsToDelete
) {
}
