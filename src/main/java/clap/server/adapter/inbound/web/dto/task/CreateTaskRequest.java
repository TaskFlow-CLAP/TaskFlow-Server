package clap.server.adapter.inbound.web.dto.task;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;


@Schema(description = "작업 생성 요청")
public record CreateTaskRequest(
        @Schema(description = "카테고리 ID")
        @NotNull
        Long categoryId,

        @Schema(description = "메인 카테고리 ID")
        @NotNull
        Long mainCategoryId,

        @Schema(description = "작업 제목")
        @NotBlank
        String title,

        @Schema(description = "작업 설명")
        String description,

        @Schema(description = "첨부 파일 URL 목록", example = "[\"https://example.com/file1.png\", \"https://example.com/file2.pdf\"]")
        List<@NotBlank String> fileUrls
) {
}
