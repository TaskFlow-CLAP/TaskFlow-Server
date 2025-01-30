package clap.server.application.port.inbound.label;

import clap.server.adapter.inbound.web.dto.common.SliceResponse;
import clap.server.adapter.inbound.web.dto.label.FindLabelListResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FindLabelListUsecase {

    SliceResponse<FindLabelListResponse> findLabelList(Long memberId, Pageable pageable);
    List<FindLabelListResponse> findLabelListAdmin(Long memberId);
}
