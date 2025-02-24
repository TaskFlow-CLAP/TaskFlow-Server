package clap.server.application.service.label;

import clap.server.adapter.inbound.web.dto.label.response.FindLabelListResponse;
import clap.server.application.mapper.response.LabelResponseMapper;
import clap.server.application.port.inbound.domain.MemberService;
import clap.server.application.port.inbound.label.FindLabelListUsecase;
import clap.server.application.port.outbound.task.LoadLabelPort;
import clap.server.common.annotation.architecture.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@ApplicationService
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FindLabelListService implements FindLabelListUsecase {

    private final LoadLabelPort loadLabelPort;
    private final MemberService memberService;

    @Override
    public List<FindLabelListResponse> findLabelList(Long memberId) {
        memberService.findActiveMember(memberId);
        return loadLabelPort.findLabelList()
                .stream()
                .map(LabelResponseMapper::toFindLabelListResponse)
                .toList();
    }
}
