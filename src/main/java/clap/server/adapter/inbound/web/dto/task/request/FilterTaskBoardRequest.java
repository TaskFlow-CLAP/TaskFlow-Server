package clap.server.adapter.inbound.web.dto.task.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record FilterTaskBoardRequest(
        @Schema(description = "라벨 ID")
        Long labelId,

        @Schema(description = "1차 카테고리 ID, ** 2차 카테고리로 검색할 시에는 1차 카테고리 값은 넣지 않습니다.")
        Long mainCategoryId,

        @Schema(description = "2차 카테고리 ID")
        Long subCategoryId,

        @Schema(description = "작업 제목", example = "작업 제목")
        String title,

        @Schema(description = "요청자 닉네임", example = "atom.park")
        String requesterNickname
) {
}
