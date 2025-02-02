package clap.server.adapter.inbound.web.dto.label;

import clap.server.adapter.outbound.persistense.entity.task.constant.LabelColor;
import io.swagger.v3.oas.annotations.media.Schema;

public record FindLabelListResponse(
        @Schema(description = "구분(label) 고유 ID")
        Long labelId,
        @Schema(description = "구분(label) 이름")
        String labelName,
        @Schema(description = "구분(label) 색상")
        LabelColor labelColor
) {
}
