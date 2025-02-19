package clap.server.adapter.inbound.web.dto.task.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;


@Schema(description = "작업 생성 요청")
public record CreateTaskRequest(
        @Schema(description = "카테고리 ID")
        @NotNull
        Long categoryId,

        @Schema(description = "작업 제목")
        @NotBlank
        String title,

        @Schema(description = "작업 설명")
        @Length(max = 1000)
        String description
) {
}
