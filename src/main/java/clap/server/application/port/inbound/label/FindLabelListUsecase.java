package clap.server.application.port.inbound.label;

import clap.server.adapter.inbound.web.dto.common.SliceResponse;
import clap.server.adapter.inbound.web.dto.label.response.FindLabelListResponse;
import org.springframework.data.domain.Pageable;

public interface FindLabelListUsecase {

    SliceResponse<FindLabelListResponse> findLabelList(Long memberId, Pageable pageable);
}
