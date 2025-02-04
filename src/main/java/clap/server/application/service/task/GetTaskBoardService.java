package clap.server.application.service.task;

import clap.server.adapter.inbound.web.dto.task.request.FilterTaskBoardRequest;
import clap.server.adapter.inbound.web.dto.task.response.TaskBoardResponse;
import clap.server.application.mapper.TaskResponseMapper;
import clap.server.application.port.inbound.domain.MemberService;
import clap.server.application.port.inbound.task.FilterTaskBoardUsecase;
import clap.server.application.port.outbound.task.LoadTaskPort;
import clap.server.common.annotation.architecture.ApplicationService;
import clap.server.domain.model.task.Task;
import clap.server.domain.policy.task.TaskPolicyConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@ApplicationService
@RequiredArgsConstructor
@Transactional(readOnly = true)
class GetTaskBoardService implements FilterTaskBoardUsecase {
    private final MemberService memberService;
    private final LoadTaskPort loadTaskPort;

    @Override
    public TaskBoardResponse getTaskBoardByFilter(Long processorId, LocalDate fromDate, FilterTaskBoardRequest request) {
        memberService.findActiveMember(processorId);
        LocalDateTime fromDateTime = fromDate != null ? fromDate.atStartOfDay() : null;
        List<Task> tasks = loadTaskPort.findTaskBoardByFilter(processorId, TaskPolicyConstants.TASK_BOARD_STATUS_FILTER, fromDateTime, request);
        return TaskResponseMapper.toTaskBoardResponse(tasks);
    }
}
