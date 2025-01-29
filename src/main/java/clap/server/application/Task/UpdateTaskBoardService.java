package clap.server.application.Task;

import clap.server.adapter.inbound.web.dto.task.request.UpdateTaskOrderRequest;
import clap.server.adapter.outbound.persistense.entity.task.constant.TaskStatus;
import clap.server.application.port.inbound.domain.MemberService;
import clap.server.application.port.inbound.domain.TaskService;
import clap.server.application.port.inbound.task.UpdateTaskBoardUsecase;
import clap.server.application.port.outbound.task.CommandTaskPort;
import clap.server.application.port.outbound.task.LoadTaskPort;
import clap.server.common.annotation.architecture.ApplicationService;
import clap.server.domain.model.member.Member;
import clap.server.domain.model.task.Task;
import clap.server.exception.ApplicationException;
import clap.server.exception.code.TaskErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@ApplicationService
@RequiredArgsConstructor
class UpdateTaskBoardService implements UpdateTaskBoardUsecase {
    private final MemberService memberService;
    private final TaskService taskService;
    private final LoadTaskPort loadTaskPort;
    private final CommandTaskPort commandTaskPort;

    private Task findByIdAndStatus(Long taskId, TaskStatus status) {
        return loadTaskPort.findByIdAndStatus(taskId, status).orElseThrow(()-> new ApplicationException(TaskErrorCode.TASK_NOT_FOUND));
    }

    /**
     * 작업(Task)의 순서를 업데이트하는 메서드
     *
     * @param processorId 작업을 수행하는 멤버 ID
     * @param request 순서 변경 요청 객체
     */
    @Override
    @Transactional
    public void updateTaskOrder(Long processorId, UpdateTaskOrderRequest request) {
        // 요청 유효성 검증
        validateRequest(request, null);
        Member processor = memberService.findActiveMember(processorId);
        Task targetTask = taskService.findById(request.targetTaskId());

        // 가장 상위로 이동
        if (request.prevTaskId() == 0) {
            moveToTop(processorId, targetTask, request.nextTaskId());
        }
        // 가장 하위로 이동
        else if (request.nextTaskId() == 0) {
            moveToBottom(processorId, targetTask, request.prevTaskId());
        } else {
            Task prevTask = findByIdAndStatus(request.prevTaskId(), targetTask.getTaskStatus());
            Task nextTask = findByIdAndStatus(request.nextTaskId(), targetTask.getTaskStatus());

            // 새로운 작업 순서 업데이트
            updateNewTaskOrder(processor.getMemberId(), targetTask, prevTask.getProcessorOrder(), nextTask.getProcessorOrder());
        }
    }

    /**
     * 순서 변경 요청의 유효성을 검증하는 메서드
     */
    private static void validateRequest(UpdateTaskOrderRequest request, TaskStatus targetStatus) {
        // 이전 및 다음 작업 ID가 모두 0인 경우 예외 발생
        if (request.prevTaskId() == 0 && request.nextTaskId() == 0) {
            throw new ApplicationException(TaskErrorCode.INVALID_TASK_STATUS_TRANSITION);
        }

        // 타겟 상태가 유효한지 검증
        if (targetStatus != null && !TaskStatus.getTaskBoardStatusList().contains(targetStatus)) {
            throw new ApplicationException(TaskErrorCode.INVALID_TASK_STATUS_TRANSITION);
        }
    }

    /**
     * 새로운 processorOrder를 계산하고 저장하는 메서드
     *
     * @param processorId 작업을 수행하는 멤버 ID
     * @param targetTask 순서를 변경할 대상 작업
     * @param prevTaskOrder 이전 작업의 processorOrder 값
     * @param nextTaskOrder 다음 작업의 processorOrder 값
     */
    private void updateNewTaskOrder(Long processorId, Task targetTask, Long prevTaskOrder, Long nextTaskOrder) {
        targetTask.updateProcessorOrder(processorId, prevTaskOrder, nextTaskOrder);
        commandTaskPort.save(targetTask);
    }

    /**
     * 작업을 가장 상위로 이동시키는 메서드
     *
     * @param processorId 작업을 수행하는 멤버 ID
     * @param targetTask 순서를 변경할 대상 작업
     * @param nextTaskId 다음 작업의 ID
     */
    private void moveToTop(Long processorId, Task targetTask, Long nextTaskId) {
        // 다음 작업 찾기
        Task nextTask = findByIdAndStatus(nextTaskId, targetTask.getTaskStatus());

        // 해당 상태에서 바로 앞에 있는 작업 찾기
        Task prevTask = loadTaskPort.findPrevOrderTaskByProcessorIdAndStatus(processorId, targetTask.getTaskStatus(), nextTask.getProcessorOrder()).orElse(null);

        Long prevTaskOrder = prevTask == null ? null : prevTask.getProcessorOrder();

        updateNewTaskOrder(processorId, targetTask, prevTaskOrder, nextTask.getProcessorOrder());
    }

    /**
     * 작업을 가장 하위로 이동시키는 메서드
     *
     * @param processorId 작업을 수행하는 멤버 ID
     * @param targetTask 순서를 변경할 대상 작업
     * @param prevTaskId 이전 작업의 ID
     */
    private void moveToBottom(Long processorId, Task targetTask, Long prevTaskId) {
        // 이전 작업 찾기
        Task prevTask = findByIdAndStatus(prevTaskId, targetTask.getTaskStatus());

        // 해당 상태에서 바로 뒤에 있는 작업 찾기
        Task nextTask = loadTaskPort.findNextOrderTaskByProcessorIdAndStatus(processorId, targetTask.getTaskStatus(), prevTask.getProcessorOrder()).orElse(null);

        Long nextTaskOrder = nextTask == null ? null : nextTask.getProcessorOrder();

        updateNewTaskOrder(processorId, targetTask, prevTask.getProcessorOrder(), nextTaskOrder);
    }

    /**
     * 작업의 상태와 순서를 동시에 변경하는 메서드
     *
     * @param processorId 작업을 수행하는 멤버 ID
     * @param request 순서 변경 요청 객체
     * @param targetStatus 변경할 작업 상태
     */
    @Override
    @Transactional
    public void updateTaskOrderAndStatus(Long processorId, UpdateTaskOrderRequest request, TaskStatus targetStatus) {
        validateRequest(request, targetStatus);

        Member processor = memberService.findActiveMember(processorId);

        Task targetTask = taskService.findById(request.targetTaskId());

        if (request.prevTaskId() == 0) {
            moveToTopWithStatusChange(processorId, targetTask, request.nextTaskId(), targetStatus);
        } else if (request.nextTaskId() == 0) {
            moveToBottomWithStatusChange(processorId, targetTask, request.prevTaskId(), targetStatus);
        } else {
            Task prevTask = findByIdAndStatus(request.prevTaskId(), targetStatus);
            Task nextTask = findByIdAndStatus(request.nextTaskId(), targetStatus);

            updateNewTaskOrderAndStatus(targetStatus, targetTask, processor.getMemberId(), prevTask.getProcessorOrder(), nextTask.getProcessorOrder());
        }
    }

    /**
     * 작업의 상태와 순서를 업데이트하는 메서드
     */
    private void updateNewTaskOrderAndStatus(TaskStatus targetStatus, Task targetTask, Long processorId, Long prevTaskOrder, Long nextTaskOrder) {
        targetTask.updateProcessorOrder(processorId, prevTaskOrder, nextTaskOrder);
        targetTask.updateTaskStatus(targetStatus);
        commandTaskPort.save(targetTask);
    }

    /**
     * 상태 변경을 포함하여 작업을 가장 상위로 이동하는 메서드
     */
    private void moveToTopWithStatusChange(Long processorId, Task targetTask, Long nextTaskId, TaskStatus targetStatus) {
        Task nextTask = findByIdAndStatus(nextTaskId, targetStatus);
        Task prevTask = loadTaskPort.findPrevOrderTaskByProcessorIdAndStatus(processorId, targetStatus, nextTask.getProcessorOrder()).orElse(null);

        Long prevTaskOrder = prevTask == null ? null : prevTask.getProcessorOrder();

        updateNewTaskOrderAndStatus(targetStatus, targetTask, processorId, prevTaskOrder, nextTask.getProcessorOrder());
    }

    /**
     * 상태 변경을 포함하여 작업을 가장 하위로 이동하는 메서드
     */
    private void moveToBottomWithStatusChange(Long processorId, Task targetTask, Long prevTaskId, TaskStatus targetStatus) {
        Task prevTask = findByIdAndStatus(prevTaskId, targetStatus);
        Task nextTask = loadTaskPort.findNextOrderTaskByProcessorIdAndStatus(processorId, targetStatus, prevTask.getProcessorOrder()).orElse(null);

        Long nextTaskOrder = nextTask == null ? null : nextTask.getProcessorOrder();

        updateNewTaskOrderAndStatus(targetStatus, targetTask, processorId, prevTask.getProcessorOrder(), nextTaskOrder);
    }
}

