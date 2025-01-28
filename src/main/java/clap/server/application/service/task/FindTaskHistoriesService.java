package clap.server.application.service.task;

import clap.server.adapter.inbound.web.dto.task.response.FindTaskHistoryResponse;
import clap.server.application.mapper.TaskHistoryMapper;
import clap.server.application.port.inbound.domain.MemberService;
import clap.server.application.port.inbound.task.FindTaskHistoriesUsecase;
import clap.server.application.port.outbound.task.LoadAttachmentPort;
import clap.server.application.port.outbound.task.LoadCommentPort;
import clap.server.application.port.outbound.task.LoadTaskHistoryPort;
import clap.server.application.port.outbound.task.LoadTaskPort;
import clap.server.common.annotation.architecture.ApplicationService;
import clap.server.domain.model.task.Attachment;
import clap.server.domain.model.task.Task;
import clap.server.domain.model.task.TaskHistory;
import clap.server.exception.DomainException;
import clap.server.exception.code.TaskErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@ApplicationService
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FindTaskHistoriesService implements FindTaskHistoriesUsecase {
    private final MemberService memberService;
    private final LoadTaskPort loadTaskPort;
    private final LoadTaskHistoryPort loadTaskHistoryPort;
    private final LoadAttachmentPort loadAttachmentPort;

    @Override
    public FindTaskHistoryResponse findTaskHistories(Long memberId, Long taskId) {
        memberService.findActiveMember(memberId);
        Task task = loadTaskPort.findById(taskId)
                .orElseThrow(()-> new DomainException(TaskErrorCode.TASK_NOT_FOUND));
        List<Attachment> attachments = loadAttachmentPort.findAllByTaskIdAndCommentIsNotNull(task.getTaskId());
        List<TaskHistory> taskHistories = loadTaskHistoryPort.findAllTaskHistoriesByTaskId(task.getTaskId());
        return TaskHistoryMapper.toFindTaskHistoryResponse(taskHistories, attachments);

        //task가 fk로 있고 comment가 null인 taskhisotry 를 가져온다
        //List<TaskHistory> taskHistoriesNotComment = loadTaskHistoryPort.findAllByTaskIdNotInComment(taskId);
        //task가 fk로 있는 comment를 가져오고 comment fk로 있는 taskhistory를 가져온다.
        //task가 fk로 있는 comment를 가져오고 comment fk로 있는 attachment와 taskhistory를 가져온다.
    }
}
