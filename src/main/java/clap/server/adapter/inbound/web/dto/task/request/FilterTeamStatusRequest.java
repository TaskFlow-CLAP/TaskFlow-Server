package clap.server.adapter.inbound.web.dto.task.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springdoc.core.annotations.ParameterObject;

import java.util.List;

@ParameterObject
public record FilterTeamStatusRequest(
        @Schema(description = "정렬 기준 (기여도순, 기본)", example = "DEFAULT")
        @NotNull
        SortBy sortBy,

        @Schema(description = "1차 카테고리 ID 목록", example = "[10, 20, 30]")
        @NotNull
        List<Long> mainCategoryIds,

        @Schema(description = "2차 카테고리 ID 목록", example = "[1, 2, 3]")
        @NotNull
        List<Long> categoryIds,

        @Schema(description = "작업 타이틀 검색", example = "타이틀1")
        @NotNull
        String taskTitle
) {
}

