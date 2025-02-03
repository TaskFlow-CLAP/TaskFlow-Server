package clap.server.adapter.inbound.web.dto.label.request;

import clap.server.adapter.outbound.persistense.entity.task.constant.LabelColor;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EditLabelRequest(

        @Schema(description = "구분(label) 이름")
        @NotBlank
        String labelName,

        @Schema(description = "구분(label) 색상")
        @NotNull
        LabelColor labelColor
) {
}
