package clap.server.application.service.history;

import clap.server.adapter.inbound.web.dto.history.response.FindTaskHistoryResponse;
import clap.server.application.mapper.response.TaskHistoryResponseMapper;
import clap.server.application.port.inbound.domain.MemberService;
import clap.server.application.port.inbound.history.FindTaskHistoriesUsecase;
import clap.server.application.port.outbound.task.LoadTaskPort;
import clap.server.application.port.outbound.taskhistory.LoadTaskHistoryPort;
import clap.server.common.annotation.architecture.ApplicationService;
import clap.server.domain.model.member.Member;
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

    @Override
    public FindTaskHistoryResponse findTaskHistories(Long memberId, Long taskId) {
        memberService.findActiveMember(memberId);
        Task task = loadTaskPort.findById(taskId)
                .orElseThrow(()-> new DomainException(TaskErrorCode.TASK_NOT_FOUND));
        List<TaskHistory> taskHistories = loadTaskHistoryPort.findAllTaskHistoriesByTaskId(task.getTaskId());
        return TaskHistoryResponseMapper.toFindTaskHistoryResponse(taskHistories);
    }
}
