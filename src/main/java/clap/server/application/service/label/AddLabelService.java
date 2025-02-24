package clap.server.application.service.label;

import clap.server.adapter.inbound.web.dto.label.request.CreateLabelRequest;
import clap.server.application.port.inbound.label.AddLabelUsecase;
import clap.server.application.port.inbound.domain.MemberService;
import clap.server.application.port.outbound.task.CommandLabelPort;
import clap.server.application.port.outbound.task.LoadLabelPort;
import clap.server.common.annotation.architecture.ApplicationService;
import clap.server.domain.model.member.Member;
import clap.server.domain.model.task.Label;
import clap.server.exception.ApplicationException;
import clap.server.exception.code.LabelErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@ApplicationService
@RequiredArgsConstructor
public class AddLabelService implements AddLabelUsecase {

    private final MemberService memberService;
    private final LoadLabelPort loadLabelPort;
    private final CommandLabelPort commandLabelPort;

    @Transactional
    @Override
    public void addLabel(Long adminId, CreateLabelRequest request) {
        Member admin = memberService.findActiveMember(adminId);
        if (loadLabelPort.existsByLabelName(request.labelName())) {
            throw new ApplicationException(LabelErrorCode.DUPLICATE_LABEL_NAME);
        }
        Label label = Label.addLabel(admin, request.labelName(), request.labelColor());
        commandLabelPort.save(label);
    }
}
