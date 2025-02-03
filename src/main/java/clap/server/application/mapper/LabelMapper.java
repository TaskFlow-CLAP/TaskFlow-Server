package clap.server.application.mapper;

import clap.server.adapter.inbound.web.dto.label.FindLabelListResponse;
import clap.server.domain.model.task.Label;

public class LabelMapper {

    private LabelMapper() {
        throw new IllegalArgumentException();
    }

    public static FindLabelListResponse toFindLabelListResponse(Label label) {
        return new FindLabelListResponse(
                label.getLabelId(),
                label.getLabelName(),
                label.getLabelColor()
        );
    }
}
