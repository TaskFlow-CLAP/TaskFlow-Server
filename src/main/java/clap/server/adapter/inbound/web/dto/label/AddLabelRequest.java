package clap.server.adapter.inbound.web.dto.label;

import clap.server.adapter.outbound.persistense.entity.task.constant.LabelColor;

public record AddLabelRequest(

        String labelName,
        LabelColor labelColor
) {}
