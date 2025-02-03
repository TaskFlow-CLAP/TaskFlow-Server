package clap.server.application.port.inbound.label;

import clap.server.adapter.inbound.web.dto.label.FindLabelListResponse;

import java.util.List;

public interface FindLabelListAdminUsecase {
    List<FindLabelListResponse> findLabelListAdmin(Long memberId);
}
