package clap.server.adapter.inbound.web.dto.task.request;


import clap.server.adapter.outbound.persistense.entity.task.constant.TaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record FilterTaskListRequest(

        @Schema(description = "검색 기간 (단위: 시간)", example = "1, 24, 168, 730, 2190 (1시간, 24시간, 1주일, 1개월, 3개월)")
        Integer term,

        @Schema(description = "카테고리 ID 목록", example = "[1, 2, 3]")
        @NotNull
        List<Long> categoryIds,

        @Schema(description = "메인 카테고리 ID 목록", example = "[10, 20, 30]")
        @NotNull
        List<Long> mainCategoryIds,

        @Schema(description = "작업 제목", example = "작업 제목")
        @NotNull
        String title,

        @Schema(description = "요청자/담당자 닉네임", example = "atom.park")
        @NotNull
        String nickName,

        @Schema(description = "작업 상태 목록", example = "[\"REQUESTED\", \"IN_PROGRESS\"]")
        @NotNull
        List<TaskStatus> taskStatus,

        @Schema(description = "정렬 기준", example = "REQUESTED_AT")
        @NotNull
        String sortBy,

        @Schema(description = "정렬 방향 (ASC/DESC)", example = "ASC")
        @NotNull
        String sortDirection
) {
}
