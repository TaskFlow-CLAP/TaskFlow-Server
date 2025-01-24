package clap.server.application.service.Task;

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
    public List<FindTaskDetailsResponse> findRequestedTaskDetails(final Long requesterId, final Long taskId) {
        memberService.findActiveMember(requesterId);
        Task task = loadTaskPort.findById(taskId)
                .orElseThrow(()-> new ApplicationException(TaskErrorCode.TASK_NOT_FOUND));
        List<Attachment> attachments = loadAttachmentPort.findAllByTaskId(taskId); //TODO: comment에 달린 이미지 첨부파일은 가져오지 않도록 수정
        return TaskMapper.toFindTaskDetailResponses(task, attachments);
    }
}
