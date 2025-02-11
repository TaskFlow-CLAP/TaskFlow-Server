package clap.server.application.service.task;

import clap.server.adapter.inbound.web.dto.task.request.UpdateTaskOrderRequest;
import clap.server.application.port.inbound.domain.MemberService;
import clap.server.application.port.inbound.domain.TaskService;
import clap.server.application.port.inbound.task.UpdateTaskBoardUsecase;
import clap.server.application.port.outbound.task.LoadTaskPort;
import clap.server.common.annotation.architecture.ApplicationService;
import clap.server.domain.model.member.Member;
import clap.server.domain.model.task.Task;
import clap.server.domain.policy.task.ProcessorValidationPolicy;
import clap.server.domain.policy.task.TaskOrderCalculationPolicy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@ApplicationService
@RequiredArgsConstructor
public class UpdateTaskBoardService implements UpdateTaskBoardUsecase {
    private final MemberService memberService;
    private final TaskService taskService;
    private final LoadTaskPort loadTaskPort;

    private final TaskOrderCalculationPolicy taskOrderCalculationPolicy;
    private final ProcessorValidationPolicy processorValidationPolicy;
    /**
     * 작업(Task)의 순서를 업데이트하는 메서드
     *
     * @param processorId 작업을 수행하는 멤버 ID
     * @param request     순서 변경 요청 객체
     */
    @Override
    @Transactional
    public void updateTaskOrder(Long processorId, UpdateTaskOrderRequest request) {
        Member processor = memberService.findActiveMember(processorId);
        Task targetTask = taskService.findById(request.targetTaskId());
        processorValidationPolicy.validateProcessor(processorId, targetTask);

        // 가장 상위로 이동
        if (request.prevTaskId() == 0) {
            Task nextTask = taskService.findByIdAndStatus(request.nextTaskId(), targetTask.getTaskStatus());
            // 해당 상태에서 바로 앞에 있는 작업 찾기
            Task prevTask = loadTaskPort.findPrevOrderTaskByProcessorOrderAndStatus(processorId, targetTask.getTaskStatus(), nextTask.getProcessorOrder()).orElse(null);
            long newOrder = taskOrderCalculationPolicy.calculateOrderForTop(prevTask, nextTask);
            updateNewTaskOrder(targetTask, newOrder);
        }
        // 가장 하위로 이동
        else if (request.nextTaskId() == 0) {
            Task prevTask = taskService.findByIdAndStatus(request.prevTaskId(), targetTask.getTaskStatus());
            // 해당 상태에서 바로 뒤에 있는 작업 찾기
            Task nextTask = loadTaskPort.findNextOrderTaskByProcessorOrderAndStatus(processorId, targetTask.getTaskStatus(), prevTask.getProcessorOrder()).orElse(null);
            long newOrder = taskOrderCalculationPolicy.calculateOrderForBottom(prevTask, nextTask);
            updateNewTaskOrder(targetTask, newOrder);
        } else {
            Task prevTask = taskService.findByIdAndStatus(request.prevTaskId(), targetTask.getTaskStatus());
            Task nextTask = taskService.findByIdAndStatus(request.nextTaskId(), targetTask.getTaskStatus());
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

}

