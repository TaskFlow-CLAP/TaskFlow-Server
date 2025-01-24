package clap.server.adapter.inbound.web.dto.task;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Schema(description = "작업 업데이트 요청")
public record UpdateTaskRequest(

        @Schema(description = "작업 ID", example = "123")
        @NotNull
        Long taskId,

        @Schema(description = "카테고리 ID", example = "1")
        @NotNull
        Long categoryId,

        @Schema(description = "메인 카테고리 ID", example = "10")
        @NotNull
        Long mainCategoryId,

        @Schema(description = "작업 제목", example = "업데이트된 제목")
        @NotBlank
        String title,

        @Schema(description = "작업 설명", example = "업데이트된 설명.")
        String description,

        @Schema(description = "첨부 파일 요청 목록", implementation = AttachmentRequest.class)
        List<AttachmentRequest> attachmentRequests
) {}

