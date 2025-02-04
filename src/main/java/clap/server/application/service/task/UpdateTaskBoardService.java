package clap.server.application.service.task;

import clap.server.adapter.inbound.web.dto.task.request.UpdateTaskOrderRequest;
import clap.server.adapter.outbound.persistense.entity.task.constant.TaskStatus;
import clap.server.application.port.inbound.domain.MemberService;
import clap.server.application.port.inbound.domain.TaskService;
import clap.server.application.port.inbound.task.UpdateTaskBoardUsecase;
import clap.server.application.port.inbound.task.UpdateTaskOrderAndStatusUsecase;
import clap.server.application.port.outbound.task.LoadTaskPort;
import clap.server.common.annotation.architecture.ApplicationService;
import clap.server.domain.model.member.Member;
import clap.server.domain.model.task.Task;
import clap.server.domain.policy.task.ProcessorValidationPolicy;
import clap.server.domain.policy.task.TaskOrderCalculationPolicy;
import clap.server.domain.policy.task.TaskPolicyConstants;
import clap.server.exception.ApplicationException;
import clap.server.exception.code.TaskErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@ApplicationService
@RequiredArgsConstructor
class UpdateTaskBoardService implements UpdateTaskBoardUsecase, UpdateTaskOrderAndStatusUsecase {
    private final MemberService memberService;
    private final TaskService taskService;
    private final LoadTaskPort loadTaskPort;
    
    private final TaskOrderCalculationPolicy taskOrderCalculationPolicy;
    private final ProcessorValidationPolicy processorValidationPolicy;

    private Task findByIdAndStatus(Long taskId, TaskStatus status) {
        return loadTaskPort.findByIdAndStatus(taskId, status).orElseThrow(() -> new ApplicationException(TaskErrorCode.TASK_NOT_FOUND));
    }

    /**
     * 작업(Task)의 순서를 업데이트하는 메서드
     *
     * @param processorId 작업을 수행하는 멤버 ID
     * @param request     순서 변경 요청 객체
     */
    @Override
    @Transactional
    public void updateTaskOrder(Long processorId, UpdateTaskOrderRequest request) {
        // 요청 유효성 검증
        validateRequest(request, null);
        Member processor = memberService.findActiveMember(processorId);
        Task targetTask = taskService.findById(request.targetTaskId());
        processorValidationPolicy.validateProcessor(processorId, targetTask);

        // 가장 상위로 이동
        if (request.prevTaskId() == 0) {
            Task nextTask = findByIdAndStatus(request.nextTaskId(), targetTask.getTaskStatus());
            // 해당 상태에서 바로 앞에 있는 작업 찾기
            Task prevTask = loadTaskPort.findPrevOrderTaskByProcessorIdAndStatus(processorId, targetTask.getTaskStatus(), nextTask.getProcessorOrder()).orElse(null);
            long newOrder = taskOrderCalculationPolicy.calculateOrderForTop(targetTask, nextTask);
            updateNewTaskOrder(targetTask, newOrder);
        }
        // 가장 하위로 이동
        else if (request.nextTaskId() == 0) {
            Task prevTask = findByIdAndStatus(request.prevTaskId(), targetTask.getTaskStatus());
            // 해당 상태에서 바로 뒤에 있는 작업 찾기
            Task nextTask = loadTaskPort.findNextOrderTaskByProcessorIdAndStatus(processorId, targetTask.getTaskStatus(), prevTask.getProcessorOrder()).orElse(null);
            long newOrder = taskOrderCalculationPolicy.calculateOrderForBottom(prevTask, nextTask);
            updateNewTaskOrder(targetTask, newOrder);
        } else {
            Task prevTask = findByIdAndStatus(request.prevTaskId(), targetTask.getTaskStatus());
            Task nextTask = findByIdAndStatus(request.nextTaskId(), targetTask.getTaskStatus());
            long newOrder = taskOrderCalculationPolicy.calculateNewProcessorOrder(prevTask.getProcessorOrder(), nextTask.getProcessorOrder());
            updateNewTaskOrder(targetTask, newOrder);
        }
    }


    /**
     * 새로운 processorOrder를 계산하고 저장하는 메서드
     *
     * @param targetTask 순서를 변경할 대상 작업
     */
    private void updateNewTaskOrder(Task targetTask, Long newOrder) {
        targetTask.updateProcessorOrder(newOrder);
        taskService.upsert(targetTask);
    }

    /**
     * 작업의 상태와 순서를 동시에 변경하는 메서드
     *
     * @param processorId  작업을 수행하는 멤버 ID
     * @param request      순서 변경 요청 객체
     * @param targetStatus 변경할 작업 상태
     */
    @Override
    @Transactional
    public void updateTaskOrderAndStatus(Long processorId, UpdateTaskOrderRequest request, TaskStatus targetStatus) {
        validateRequest(request, targetStatus);
        Member processor = memberService.findActiveMember(processorId);
        Task targetTask = taskService.findById(request.targetTaskId());
        processorValidationPolicy.validateProcessor(processorId, targetTask);

        if (request.prevTaskId() == 0) {
            Task nextTask = findByIdAndStatus(request.nextTaskId(), targetStatus);
            // 해당 상태에서 바로 앞 있는 작업 찾기
            Task prevTask = loadTaskPort.findPrevOrderTaskByProcessorIdAndStatus(processorId, targetStatus, nextTask.getProcessorOrder()).orElse(null);
            long newOrder = taskOrderCalculationPolicy.calculateOrderForTop(prevTask,nextTask);
            updateNewTaskOrderAndStatus(targetStatus, targetTask, newOrder);
        } else if (request.nextTaskId() == 0) {
            Task prevTask = findByIdAndStatus(request.prevTaskId(), targetStatus);
            // 해당 상태에서 바로 뒤에 있는 작업 찾기
            Task nextTask = loadTaskPort.findNextOrderTaskByProcessorIdAndStatus(processorId, targetStatus, prevTask.getProcessorOrder()).orElse(null);
            long newOrder = taskOrderCalculationPolicy.calculateOrderForBottom(prevTask, nextTask);
            updateNewTaskOrderAndStatus(targetStatus, targetTask, newOrder);
        } else {
            Task prevTask = findByIdAndStatus(request.prevTaskId(), targetStatus);
            Task nextTask = findByIdAndStatus(request.nextTaskId(), targetStatus);
            long newOrder = taskOrderCalculationPolicy.calculateNewProcessorOrder(nextTask.getProcessorOrder(), prevTask.getProcessorOrder());
            updateNewTaskOrderAndStatus(targetStatus, targetTask, newOrder);
        }
    }

    /**
     * 작업의 상태와 순서를 업데이트하는 메서드
     */
    private void updateNewTaskOrderAndStatus(TaskStatus targetStatus, Task targetTask, long newOrder) {
        targetTask.updateProcessorOrder(newOrder);
        targetTask.updateTaskStatus(targetStatus);
        taskService.upsert(targetTask);
    }

    /**
     * 순서 변경 요청의 유효성을 검증하는 메서드
     */
    public void validateRequest(UpdateTaskOrderRequest request, TaskStatus targetStatus) {
        // 이전 및 다음 작업 ID가 모두 0인 경우 예외 발생
        if (request.prevTaskId() == 0 && request.nextTaskId() == 0) {
            throw new ApplicationException(TaskErrorCode.INVALID_TASK_STATUS_TRANSITION);
        }

        // 타겟 상태가 유효한지 검증
        if (targetStatus != null && !TaskPolicyConstants.TASK_BOARD_STATUS_FILTER.contains(targetStatus)) {
            throw new ApplicationException(TaskErrorCode.INVALID_TASK_STATUS_TRANSITION);
        }
    }

}

