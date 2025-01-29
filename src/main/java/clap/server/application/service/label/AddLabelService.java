package clap.server.application.service.label;

import clap.server.adapter.inbound.web.dto.label.AddLabelRequest;
import clap.server.application.port.inbound.admin.AddLabelUsecase;
import clap.server.application.port.inbound.domain.MemberService;
import clap.server.application.port.outbound.task.CommandLabelPort;
import clap.server.common.annotation.architecture.ApplicationService;
import clap.server.domain.model.member.Member;
import clap.server.domain.model.task.Label;
import lombok.RequiredArgsConstructor;

@ApplicationService
@RequiredArgsConstructor
public class AddLabelService implements AddLabelUsecase {

    private final MemberService memberService;
    private final CommandLabelPort commandLabelPort;

    @Override
    public void addLabel(Long adminId, AddLabelRequest request) {
        Member admin = memberService.findActiveMember(adminId);
        Label label = Label.addLabel(admin, request);
        commandLabelPort.save(label);
    }
}
