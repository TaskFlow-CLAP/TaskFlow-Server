package clap.server.adapter.inbound.web.dto.label;

import clap.server.adapter.outbound.persistense.entity.task.constant.LabelColor;

public record FindLabelListResponse(
        Long labelId,
        String labelName,
        LabelColor labelColor
) {
}
