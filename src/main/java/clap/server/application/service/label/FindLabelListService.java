package clap.server.application.service.label;

import clap.server.adapter.inbound.web.dto.common.SliceResponse;
import clap.server.adapter.inbound.web.dto.label.FindLabelListResponse;
import clap.server.application.mapper.LabelMapper;
import clap.server.application.port.inbound.domain.MemberService;
import clap.server.application.port.inbound.label.FindLabelListUsecase;
import clap.server.application.port.outbound.task.LoadLabelPort;
import clap.server.common.annotation.architecture.ApplicationService;
import clap.server.domain.model.member.Member;
import clap.server.exception.ApplicationException;
import clap.server.exception.code.MemberErrorCode;
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
    public SliceResponse<FindLabelListResponse> findLabelList(Long userId, Pageable pageable) {
        Member member = memberService.findActiveMember(userId);

        // 담당자인데 검토자가 아닌 경우 Error 처리
        if (!member.getMemberInfo().isReviewer()) {
            throw new ApplicationException(MemberErrorCode.NOT_A_REVIEWER);
        }
        return loadLabelPort.findLabelListBySlice(pageable);
    }

    @Override
    public List<FindLabelListResponse> findLabelListAdmin(Long userId) {
        memberService.findActiveMember(userId);
        return loadLabelPort.findLabelList()
                .stream()
                .map(LabelMapper::toFindLabelListResponse)
                .toList();
    }
}
