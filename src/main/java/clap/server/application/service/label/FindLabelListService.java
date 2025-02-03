package clap.server.application.service.label;

import clap.server.adapter.inbound.web.dto.common.SliceResponse;
import clap.server.adapter.inbound.web.dto.label.response.FindLabelListResponse;
import clap.server.application.mapper.LabelMapper;
import clap.server.application.port.inbound.domain.MemberService;
import clap.server.application.port.inbound.label.FindLabelListAdminUsecase;
import clap.server.application.port.inbound.label.FindLabelListUsecase;
import clap.server.application.port.outbound.task.LoadLabelPort;
import clap.server.common.annotation.architecture.ApplicationService;
import clap.server.domain.model.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@ApplicationService
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FindLabelListService implements FindLabelListUsecase {

    private final LoadLabelPort loadLabelPort;
    private final MemberService memberService;

    @Override
    public List<FindLabelListResponse> findLabelList(Long userId) {
        memberService.findActiveMember(userId);
        return loadLabelPort.findLabelList()
                .stream()
                .map(LabelMapper::toFindLabelListResponse)
                .toList();
    }
}
