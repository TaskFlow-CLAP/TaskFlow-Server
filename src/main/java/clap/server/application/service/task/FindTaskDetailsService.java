package clap.server.application.service.task;

import clap.server.adapter.inbound.web.dto.task.response.FindTaskDetailsForManagerResponse;
import clap.server.adapter.inbound.web.dto.task.response.FindTaskDetailsResponse;
import clap.server.application.mapper.response.TaskResponseMapper;
import clap.server.application.port.inbound.domain.MemberService;
import clap.server.application.port.inbound.task.FindTaskDetailsUsecase;
import clap.server.application.port.outbound.task.LoadAttachmentPort;
import clap.server.application.port.outbound.task.LoadTaskPort;
import clap.server.common.annotation.architecture.ApplicationService;
import clap.server.domain.model.task.Attachment;
import clap.server.domain.model.task.Task;
import clap.server.exception.ApplicationException;
import clap.server.exception.code.TaskErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@ApplicationService
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FindTaskDetailsService implements FindTaskDetailsUsecase {
    private final MemberService memberService;
    private final LoadTaskPort loadTaskPort;
    private final LoadAttachmentPort loadAttachmentPort;

    @Override
    public FindTaskDetailsResponse findRequestedTaskDetails(final Long requesterId, final Long taskId) {
        memberService.findActiveMember(requesterId);
        Task task = loadTaskPort.findById(taskId)
                .orElseThrow(()-> new ApplicationException(TaskErrorCode.TASK_NOT_FOUND));
        List<Attachment> attachments = loadAttachmentPort.findAllByTaskId(taskId);
        return TaskResponseMapper.toFindTaskDetailResponse(task, attachments);
    }

    @Override
    public FindTaskDetailsForManagerResponse findTaskDetailsForManager(final Long requesterId, final Long taskId) {
        memberService.findActiveMember(requesterId);
        Task task = loadTaskPort.findById(taskId)
                .orElseThrow(() -> new ApplicationException(TaskErrorCode.TASK_NOT_FOUND));
        List<Attachment> attachments = loadAttachmentPort.findAllByTaskId(taskId);
        return TaskResponseMapper.toFindTaskDetailForManagerResponse(task, attachments);
    }
}
