package clap.server.adapter.inbound.web.dto.admin.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record FindMainCategoryResponse(
        @Schema(description = "메인 카테고리 ID", example = "1")
        Long mainCategoryId,

        @Schema(description = "카테고리 이름", example = "VM 관련")
        String name,

        @Schema(description = "카테고리 코드", example = "VM")
        String code
) {
}
