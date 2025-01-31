package clap.server.application.port.outbound.task;

import clap.server.adapter.inbound.web.dto.common.SliceResponse;
import clap.server.adapter.inbound.web.dto.label.FindLabelListResponse;
import clap.server.domain.model.task.Label;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface LoadLabelPort {

    Optional<Label> findById(Long labelId);

    List<Label> findLabelList();

    SliceResponse<FindLabelListResponse> findLabelListBySlice(Pageable pageable);




}
