package clap.server.application.mapper;

import clap.server.adapter.inbound.web.dto.common.SliceResponse;
import clap.server.adapter.inbound.web.dto.label.response.FindLabelListResponse;
import clap.server.domain.model.task.Label;
import org.springframework.data.domain.Slice;

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

    public static SliceResponse<FindLabelListResponse> toSliceOfFindNoticeListResponse(Slice<FindLabelListResponse> slice) {
        return new SliceResponse<>(
                slice.getContent(),
                slice.hasNext(),
                slice.isFirst(),
                slice.isLast()
        );
    }
}
