package clap.server.application.service.label;

import clap.server.adapter.inbound.web.dto.label.request.EditLabelRequest;
import clap.server.application.port.inbound.admin.DeleteLabelUsecase;
import clap.server.application.port.inbound.label.UpdateLabelUsecase;
import clap.server.application.port.inbound.domain.MemberService;
import clap.server.application.port.outbound.task.CommandLabelPort;
import clap.server.application.port.outbound.task.LoadLabelPort;
import clap.server.common.annotation.architecture.ApplicationService;
import clap.server.domain.model.task.Label;
import clap.server.exception.ApplicationException;
import clap.server.exception.code.LabelErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@ApplicationService
@RequiredArgsConstructor
public class ManageLabelService implements UpdateLabelUsecase, DeleteLabelUsecase {

    private final MemberService memberService;
    private final LoadLabelPort loadLabelPort;
    private final CommandLabelPort commandLabelPort;

    @Transactional
    @Override
    public void editLabel(Long adminId, Long labelId, EditLabelRequest request) {
        memberService.findActiveMember(adminId);

        Label label = loadLabelPort.findById(labelId)
                .orElseThrow(() -> new ApplicationException(LabelErrorCode.LABEL_NOT_FOUND));

        loadLabelPort.existsByLabelName(request.labelName());

        label.updateLabel(request);
        commandLabelPort.save(label);
    }


    @Transactional
    @Override
    public void deleteLabel(Long adminId, Long labelId) {
        memberService.findActiveMember(adminId);

        Label label = loadLabelPort.findById(labelId)
                .orElseThrow(() -> new ApplicationException(LabelErrorCode.LABEL_NOT_FOUND));

        label.deleteLabel();
        commandLabelPort.save(label);
    }
}
