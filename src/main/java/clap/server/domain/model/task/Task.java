package clap.server.domain.model.task;

import clap.server.adapter.outbound.persistense.entity.task.constant.TaskStatus;
import clap.server.domain.model.common.BaseTime;
import clap.server.domain.model.member.Member;
import clap.server.exception.DomainException;
import clap.server.exception.code.TaskErrorCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

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

    public void updateTask(TaskStatus status, Category category, String title, String description) {
        if (status != TaskStatus.REQUESTED) {
            throw new DomainException(TaskErrorCode.TASK_STATUS_MISMATCH);
        }
        this.category = category;
        this.title = title;
        this.description = description;
        this.taskCode = toTaskCode(category);
    }

    public void setInitialProcessorOrder() {
        if(this.processor == null) {
            this.processorOrder = this.taskId * 128L;
        }
    }

    public void updateTaskStatus(TaskStatus status) {
        this.taskStatus = status;
    }

    public void updateProcessor(Member processor) {
        this.processor = processor;
    }

    public void approveTask(Member reviewer, Member processor, LocalDateTime dueDate, Category category, Label label) {
        this.reviewer = reviewer;
        this.processor = processor;
        this.dueDate = dueDate;
        this.category = category;
        this.label = label;
        this.taskStatus = TaskStatus.IN_PROGRESS;
    }

    private static String toTaskCode(Category category){
        return category.getMainCategory().getCode() + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHmm"));
    }
}
