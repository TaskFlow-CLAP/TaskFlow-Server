package clap.server.adapter.inbound.web.dto.task.request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record FilterTeamStatusRequest(
        @Schema(description = "정렬 기준 (기여도순, 기본)", example = "기여도순")
        String sortBy,

        @Schema(description = "1차 카테고리 ID 목록", example = "[10, 20, 30]")
        List<Long> mainCategoryIds,

        @Schema(description = "2차 카테고리 ID 목록", example = "[1, 2, 3]")
        List<Long> categoryIds,

        @Schema(description = "작업 타이틀 검색", example = "타이틀1")
        String taskTitle
) {
        public FilterTeamStatusRequest {
                sortBy = (sortBy == null || sortBy.isEmpty()) ? "기본" : sortBy;
                mainCategoryIds = mainCategoryIds == null ? List.of() : mainCategoryIds;
                categoryIds = categoryIds == null ? List.of() : categoryIds;
                taskTitle = taskTitle == null ? "" : taskTitle;
        }

}
