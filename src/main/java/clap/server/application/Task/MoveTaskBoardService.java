package clap.server.application.Task;

import clap.server.adapter.inbound.web.dto.task.request.UpdateTaskOrderRequest;
import clap.server.adapter.outbound.persistense.entity.task.constant.TaskStatus;
import clap.server.application.port.inbound.domain.MemberService;
import clap.server.application.port.inbound.domain.TaskService;
import clap.server.application.port.inbound.task.MoveTaskBoardUsecase;
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
class MoveTaskBoardService implements MoveTaskBoardUsecase {
    private final MemberService memberService;
    private final TaskService taskService;
    private final LoadTaskPort loadTaskPort;
    private final CommandTaskPort commandTaskPort;

    @Override
    @Transactional
    public void updateTaskOrder(Long processorId, UpdateTaskOrderRequest request) {
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
            Task prevTask = taskService.findById(request.prevTaskId());
            Task nextTask = taskService.findById(request.nextTaskId());
            // 같은 상태에서의 순서 변경인지 검증
            Task.checkTaskStatusIsSame(targetTask, prevTask, nextTask);
            updateNewTaskOrder(processor.getMemberId(), targetTask, prevTask.getProcessorOrder(), nextTask.getProcessorOrder());
        }
    }

    private static void validateRequest(UpdateTaskOrderRequest request, TaskStatus targetStatus) {
        if(request.prevTaskId()==0 && request.nextTaskId()==0){
            throw new ApplicationException(TaskErrorCode.INVALID_TASK_STATUS_TRANSITION);
        }
        if(targetStatus != null && !TaskStatus.getTaskBoardStatusList().contains(targetStatus)){
            throw new ApplicationException(TaskErrorCode.INVALID_TASK_STATUS_TRANSITION);
        }
    }

    private void updateNewTaskOrder(Long processorId, Task targetTask, Long prevTaskOrder, Long nextTaskOrder) {
        targetTask.updateProcessorOrder(processorId, prevTaskOrder, nextTaskOrder);
        commandTaskPort.save(targetTask);
    }

    private void moveToTop(Long processorId, Task targetTask, Long nextTaskId) {
        Task nextTask = taskService.findById(nextTaskId);
        Task prevTask = loadTaskPort.findPrevOrderTaskByProcessorIdAndStatus(processorId, targetTask.getTaskStatus(), nextTask.getProcessorOrder()).orElse(null);
        Long prevTaskOrder = prevTask==null? null: prevTask.getTaskId();
        updateNewTaskOrder(processorId, targetTask, prevTaskOrder, nextTask.getProcessorOrder());
    }

    private void moveToBottom(Long processorId, Task targetTask, Long prevTaskId) {
        Task prevTask = taskService.findById(prevTaskId);
        Task nextTask = loadTaskPort.findNextOrderTaskByProcessorIdAndStatus(processorId, targetTask.getTaskStatus(), prevTask.getProcessorOrder()).orElse(null);
        Long nextTaskOrder = nextTask==null? null: nextTask.getProcessorOrder();
        log.info("바닥 전환 진입 {}: ", nextTaskOrder);
        updateNewTaskOrder(processorId, targetTask, prevTask.getProcessorOrder(), nextTaskOrder);
    }


    @Override
    @Transactional
    public void updateTaskOrderAndStatus(Long processorId, UpdateTaskOrderRequest request, TaskStatus targetStatus) {
        log.info("상태 전환 진입 {}: ", targetStatus);
        validateRequest(request, targetStatus);
        Member processor = memberService.findActiveMember(processorId);
        Task targetTask = taskService.findById(request.targetTaskId());

        // 가장 상위로 이동
        if (request.prevTaskId() == 0) {
            moveToTopWithStatusChange(processorId, targetTask, request.nextTaskId(), targetStatus);
        }
        // 가장 하위로 이동
        else if (request.nextTaskId() == 0) {
            moveToBottomWithStatusChange(processorId, targetTask, request.prevTaskId(), targetStatus);
        } else {
            Task prevTask = taskService.findById(request.prevTaskId());
            Task nextTask = taskService.findById(request.nextTaskId());
            // 다른 상태로의 상태 전환인지 검증
            Task.validateTaskStatusTransition(prevTask, nextTask, targetStatus);
            updateNewTaskOrderAndStatus(targetStatus, targetTask, processor.getMemberId(), prevTask.getProcessorOrder(), nextTask.getProcessorOrder());
        }
    }

    private void updateNewTaskOrderAndStatus(TaskStatus targetStatus, Task targetTask, Long processorId, Long prevTaskOrder, Long nextTaskOrder) {
        targetTask.updateProcessorOrder(processorId, prevTaskOrder, nextTaskOrder);
        targetTask.changeTaskStatus(targetStatus);
        commandTaskPort.save(targetTask);
    }

    private void moveToTopWithStatusChange(Long processorId, Task targetTask, Long nextTaskId, TaskStatus targetStatus) {
        Task nextTask = taskService.findById(nextTaskId);
        Task prevTask = loadTaskPort.findPrevOrderTaskByProcessorIdAndStatus(processorId, targetStatus, nextTask.getProcessorOrder()).orElse(null);
        Long prevTaskOrder = prevTask==null? null: prevTask.getTaskId();
        updateNewTaskOrderAndStatus(targetStatus, targetTask, processorId, prevTaskOrder, nextTask.getProcessorOrder());
    }

    private void moveToBottomWithStatusChange(Long processorId, Task targetTask, Long prevTaskId, TaskStatus targetStatus) {
        Task prevTask = taskService.findById(prevTaskId);
        Task nextTask = loadTaskPort.findNextOrderTaskByProcessorIdAndStatus(processorId, targetStatus, prevTask.getProcessorOrder()).orElse(null);
        Long nextTaskOrder = nextTask==null? null: nextTask.getProcessorOrder();
        updateNewTaskOrderAndStatus(targetStatus, targetTask, processorId, prevTask.getProcessorOrder(), nextTaskOrder);
    }

}
