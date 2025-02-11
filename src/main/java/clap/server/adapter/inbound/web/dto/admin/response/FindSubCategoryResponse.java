package clap.server.adapter.inbound.web.dto.admin.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record FindSubCategoryResponse(
        @Schema(description = "서브 카테고리 ID", example = "4")
        Long subCategoryId,

        @Schema(description = "메인 카테고리 ID", example = "1")
        Long mainCategoryId,

        @Schema(description = "카테고리 이름", example = "VM 수정")
        String name,

        @Schema(description = "카테고리 코드", example = "VU")
        String code,

        @Schema(description = "카테고리 설명 예시", example = "VM을 수정합니다.")
        String descriptionExample
) {
}
