package clap.server.application.mapper;

import clap.server.adapter.inbound.web.dto.label.response.FindLabelListResponse;
import clap.server.domain.model.task.Label;

public class LabelResponseMapper {

    private LabelResponseMapper() {
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
