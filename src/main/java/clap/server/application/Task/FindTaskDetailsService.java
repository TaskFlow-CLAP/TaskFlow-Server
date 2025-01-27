package clap.server.application.Task;

import clap.server.adapter.inbound.web.dto.task.FindTaskDetailsForManagerResponse;
import clap.server.adapter.inbound.web.dto.task.FindTaskDetailsResponse;
import clap.server.adapter.outbound.persistense.entity.task.TaskEntity;
import clap.server.application.mapper.TaskMapper;
import clap.server.application.port.inbound.domain.MemberService;
import clap.server.application.port.inbound.task.FindTaskDetailsUsecase;
import clap.server.application.port.outbound.task.LoadAttachmentPort;
import clap.server.application.port.outbound.task.LoadTaskPort;
import clap.server.common.annotation.architecture.ApplicationService;
import clap.server.domain.model.member.Member;
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
        List<Attachment> attachments = loadAttachmentPort.findAllByTaskIdAndCommentIsNull(taskId);
        return TaskMapper.toFindTaskDetailResponse(task, attachments);
    }

    @Override
    public FindTaskDetailsForManagerResponse findTaskDetailsForManager(final Long requesterId, final Long taskId) {
        memberService.findActiveMember(requesterId);
        Task task = loadTaskPort.findById(taskId)
                .orElseThrow(() -> new ApplicationException(TaskErrorCode.TASK_NOT_FOUND));
        List<Attachment> attachments = loadAttachmentPort.findAllByTaskIdAndCommentIsNull(taskId);
        return TaskMapper.toFindTaskDetailForManagerResponse(task, attachments);
    }
}
