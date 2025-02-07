package clap.server.domain.model.task;

import clap.server.adapter.outbound.persistense.entity.task.constant.TaskStatus;
import clap.server.domain.model.common.BaseTime;
import clap.server.domain.model.member.Member;
import clap.server.exception.ApplicationException;
import clap.server.exception.DomainException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import static clap.server.domain.policy.task.TaskPolicyConstants.DEFAULT_PROCESSOR_ORDER_GAP;
import static clap.server.exception.code.TaskErrorCode.NOT_A_REQUESTER;

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
    private Long agitPostId;
    private Member processor;
    private Label label;
    private Member reviewer;
    private LocalDateTime dueDate;
    private LocalDateTime finishedAt;
    private int attachmentCount;

    public static Task createTask(Member member, Category category, String title, String description, int attachmentCount) {
        return Task.builder()
                .requester(member)
                .category(category)
                .title(title)
                .description(description)
                .taskStatus(TaskStatus.REQUESTED)
                .taskCode(toTaskCode(category))
                .attachmentCount(attachmentCount)
                .build();
    }

    public void updateTask(Long requesterId, Category category, String title, String description, int attachmentCount) {
        if (!Objects.equals(requesterId, this.requester.getMemberId())) {
            throw new ApplicationException(NOT_A_REQUESTER);
        }
        this.category = category;
        this.title = title;
        this.description = description;
        this.taskCode = toTaskCode(category);
        this.attachmentCount = attachmentCount;
    }

    public void finalSave(int attachmentCount) {
        if (this.processor == null) {
            this.processorOrder = this.taskId * DEFAULT_PROCESSOR_ORDER_GAP;
        }
        this.attachmentCount = attachmentCount;
    }

    public void updateTaskStatus(TaskStatus status) {
        if (status.equals(TaskStatus.COMPLETED)) {
            this.finishedAt = LocalDateTime.now();
        }
        this.taskStatus = status;
    }

    public void updateAgitPostId(Long agitPostId) {
        this.agitPostId = agitPostId;
    }

    public void terminateTask() {
        this.taskStatus = TaskStatus.TERMINATED;
        this.finishedAt = LocalDateTime.now();
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

    public void updateProcessorOrder(long newProcessorOrder) {
        this.processorOrder = newProcessorOrder;
    }

    public void cancelTask(Long requesterId) {
        if (!Objects.equals(this.requester.getMemberId(), requesterId)) {
            throw new DomainException(NOT_A_REQUESTER);
        }
        this.taskStatus = TaskStatus.TERMINATED;
        this.finishedAt = LocalDateTime.now();
    }
}
