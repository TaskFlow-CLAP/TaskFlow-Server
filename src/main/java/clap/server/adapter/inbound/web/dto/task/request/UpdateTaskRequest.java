package clap.server.adapter.inbound.web.dto.task.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Schema(description = "작업 업데이트 요청")
public record UpdateTaskRequest(

        @Schema(description = "카테고리 ID", example = "1")
        @NotNull
        Long categoryId,

        @Schema(description = "작업 제목", example = "업데이트된 제목")
        @NotBlank
        String title,

        @Schema(description = "작업 설명", example = "업데이트된 설명.")
        String description,

        @Schema(description = "삭제할 파일 ID 목록, 없을 경우 emptylist 전송")
        @NotNull
        List<Long> attachmentsToDelete
) {}

