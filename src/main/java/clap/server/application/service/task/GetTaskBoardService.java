package clap.server.application.service.task;

import clap.server.adapter.inbound.web.dto.task.request.FilterTaskBoardRequest;
import clap.server.adapter.inbound.web.dto.task.response.TaskBoardResponse;
import clap.server.adapter.outbound.persistense.entity.task.constant.TaskStatus;
import clap.server.application.mapper.TaskMapper;
import clap.server.application.port.inbound.domain.MemberService;
import clap.server.application.port.inbound.task.FilterTaskBoardUsecase;
import clap.server.application.port.inbound.task.GetTaskBoardUsecase;
import clap.server.application.port.outbound.task.LoadTaskPort;
import clap.server.common.annotation.architecture.ApplicationService;
import clap.server.domain.model.task.Task;
import clap.server.domain.policy.task.TaskValuePolicy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@ApplicationService
@RequiredArgsConstructor
@Transactional(readOnly = true)
class GetTaskBoardService implements GetTaskBoardUsecase, FilterTaskBoardUsecase {

    private final MemberService memberService;
    private final LoadTaskPort loadTaskPort;

    @Override
    public TaskBoardResponse getTaskBoards(Long processorId, LocalDate untilDate, Pageable pageable) {
        memberService.findActiveMember(processorId);
        LocalDateTime untilDateTime = untilDate == null ? LocalDate.now().plusDays(1).atStartOfDay() : untilDate.plusDays(1).atStartOfDay();
        Slice<Task> tasks = loadTaskPort.findByProcessorAndStatus(processorId, List.of(TaskValuePolicy.TASK_BOARD_STATUS_FILTER), untilDateTime, pageable);
        return TaskMapper.toSliceTaskItemResponse(tasks);
    }

    @Override
    public TaskBoardResponse getTaskBoardByFilter(Long processorId, LocalDate untilDate, FilterTaskBoardRequest request, Pageable pageable) {
        memberService.findActiveMember(processorId);
        LocalDateTime untilDateTime = untilDate == null ? LocalDate.now().plusDays(1).atStartOfDay() : untilDate.plusDays(1).atStartOfDay();
        Slice<Task> tasks = loadTaskPort.findTaskBoardByFilter(processorId, List.of(TaskValuePolicy.TASK_BOARD_STATUS_FILTER), untilDateTime, request, pageable);
        return TaskMapper.toSliceTaskItemResponse(tasks);
    }
}
