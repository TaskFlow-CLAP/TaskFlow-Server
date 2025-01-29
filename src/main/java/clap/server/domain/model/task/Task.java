package clap.server.domain.model.task;

import clap.server.adapter.outbound.persistense.entity.task.constant.TaskStatus;
import clap.server.domain.model.common.BaseTime;
import clap.server.domain.model.member.Member;
import clap.server.exception.ApplicationException;
import clap.server.exception.DomainException;
import clap.server.exception.code.TaskErrorCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import static clap.server.domain.model.task.constants.TaskProcessorOrderPolicy.DEFAULT_PROCESSOR_ORDER_GAP;

@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Task extends BaseTime {
    private Long taskId;
    private String taskCode;
    private String title;
    private String description;
    private Category category;
    private Member requester;
    private TaskStatus taskStatus;
    private long processorOrder;
    private Member processor;
    private Label label;
    private Member reviewer;
    private LocalDateTime dueDate;
    private LocalDateTime finishedAt;

    public static Task createTask(Member member, Category category, String title, String description) {
        return Task.builder()
                .requester(member)
                .category(category)
                .title(title)
                .description(description)
                .taskStatus(TaskStatus.REQUESTED)
                .taskCode(toTaskCode(category))
                .build();
    }

    public void updateTask(Long requesterId, Category category, String title, String description) {
        if(!Objects.equals(requesterId, this.requester.getMemberId() )) {
            throw new ApplicationException(TaskErrorCode.NOT_A_REQUESTER);
        }
        if (this.taskStatus != TaskStatus.REQUESTED) {
            throw new DomainException(TaskErrorCode.TASK_STATUS_MISMATCH);
        }
        this.category = category;
        this.title = title;
        this.description = description;
        this.taskCode = toTaskCode(category);
    }

    public void setInitialProcessorOrder() {
        if (this.processor == null) {
            this.processorOrder = this.taskId * DEFAULT_PROCESSOR_ORDER_GAP;
        }
    }

    public void updateTaskStatus(TaskStatus status) {
        if (status == null) {
            throw new DomainException(TaskErrorCode.INVALID_TASK_STATUS_TRANSITION);
        }
        this.taskStatus = status;
    }

    public void updateProcessor(Member processor) {
        this.processor = processor;
    }

    public void updateLabel(Label label) {
        this.label = label;
    }

    public void approveTask(Member reviewer, Member processor, LocalDateTime dueDate, Category category, Label label) {
        this.reviewer = reviewer;
        this.processor = processor;
        this.dueDate = dueDate;
        this.category = category;
        this.label = label;
        this.taskStatus = TaskStatus.IN_PROGRESS;
    }

    private static String toTaskCode(Category category) {
        return category.getMainCategory().getCode() + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHmm"));
    }

    public void updateProcessorOrder(Long processorId, Long prevTaskOrder, Long nextTaskOrder) {
        if (!Objects.equals(processorId, this.processor.getMemberId())) {
            throw new DomainException(TaskErrorCode.NOT_A_PROCESSOR);
        }
        long newProcessorOrder;

        // 최상위 이동: 가장 작은 processorOrder보다 더 작은 값 설정
        if (prevTaskOrder == null && nextTaskOrder != null) {
            newProcessorOrder = nextTaskOrder - DEFAULT_PROCESSOR_ORDER_GAP;
        }
        // 최하위 이동: 가장 큰 processorOrder보다 더 큰 값 설정
        else if (prevTaskOrder != null && nextTaskOrder == null) {
            newProcessorOrder = prevTaskOrder + DEFAULT_PROCESSOR_ORDER_GAP;
        }
        // 중간 위치로 이동: prevTask와 nextTask의 processorOrder 평균값 사용
        else if (prevTaskOrder != null && nextTaskOrder != null) {
            if (nextTaskOrder - prevTaskOrder < 2) {
                throw new DomainException(TaskErrorCode.INVALID_TASK_ORDER);
            }
            newProcessorOrder = (prevTaskOrder + nextTaskOrder) / 2;
        }
        // 기본값 (예외적인 상황 방지)
        else {
            newProcessorOrder = DEFAULT_PROCESSOR_ORDER_GAP;
        }
        this.processorOrder = newProcessorOrder;
    }

}
