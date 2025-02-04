package clap.server.adapter.inbound.web.dto.task.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record FilterTaskBoardRequest(
        @Schema(description = "라벨 ID")
        Long labelId,

        @Schema(description = "1차 카테고리 ID 목록", example = "[1, 3, 5]")
        @NotNull
        List<Long> mainCategoryIds,

        @Schema(description = "2차 카테고리 ID 목록", example = "[2, 4]")
        @NotNull
        List<Long> categoryIds,

        @Schema(description = "작업 제목", example = "작업 제목")
        String title,

        @Schema(description = "요청자 닉네임", example = "atom.park")
        String requesterNickname
) {
}
