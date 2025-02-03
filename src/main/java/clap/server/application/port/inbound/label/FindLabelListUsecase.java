package clap.server.application.port.inbound.label;

import clap.server.adapter.inbound.web.dto.label.response.FindLabelListResponse;

import java.util.List;

public interface FindLabelListUsecase {

    List<FindLabelListResponse> findLabelList(Long memberId);
}
