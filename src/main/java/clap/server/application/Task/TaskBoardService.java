package clap.server.application.Task;

import clap.server.adapter.inbound.web.dto.task.response.TaskBoardResponse;
import clap.server.adapter.outbound.persistense.entity.task.constant.TaskStatus;
import clap.server.application.mapper.TaskMapper;
import clap.server.application.port.inbound.domain.MemberService;
import clap.server.application.port.inbound.domain.TaskService;
import clap.server.application.port.inbound.task.TaskBoardUsecase;
import clap.server.application.port.outbound.task.CommandTaskPort;
import clap.server.application.port.outbound.task.LoadTaskPort;
import clap.server.common.annotation.architecture.ApplicationService;
import clap.server.domain.model.task.Task;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@ApplicationService
@RequiredArgsConstructor
class TaskBoardService implements TaskBoardUsecase {
    private final static List<TaskStatus> VIEWABLE_STATUSES = List.of(
            TaskStatus.IN_PROGRESS,
            TaskStatus.PENDING_COMPLETED,
            TaskStatus.COMPLETED
    );
    private final MemberService memberService;
    private final TaskService taskService;
    private final LoadTaskPort loadTaskPort;
    private final CommandTaskPort commandTaskPort;

    @Transactional(readOnly = true)
    @Override
    public TaskBoardResponse getTaskBoards(Long processorId, LocalDate untilDate, Pageable pageable) {
        memberService.findById(processorId);
        LocalDateTime untilDateTime = untilDate==null ? LocalDate.now().plusDays(1).atStartOfDay() : untilDate.plusDays(1).atStartOfDay();
        Slice<Task> tasks = loadTaskPort.findByProcessorAndStatus(processorId, VIEWABLE_STATUSES, untilDateTime, pageable);
        return TaskMapper.toSliceTaskItemResponse(tasks);
    }

}
