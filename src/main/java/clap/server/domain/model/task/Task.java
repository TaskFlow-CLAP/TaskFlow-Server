package clap.server.domain.model.task;

import clap.server.adapter.outbound.persistense.entity.task.constant.TaskStatus;
import clap.server.domain.model.common.BaseTime;
import clap.server.domain.model.member.Member;
import clap.server.exception.ApplicationException;
import clap.server.exception.code.MemberErrorCode;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
    private int processorOrder;
    private Member processor;
    private Label label;
    private Member reviewer;
    private LocalDateTime dueDate;
    private LocalDateTime completedAt;

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

    public void updateTask(Category category, String title, String description) {
        this.category = category;
        this.title = title;
        this.description = description;
        this.taskCode = toTaskCode(category);
    }

    public void approveTask(Member reviewer, Member processor, LocalDateTime dueDate, Category mainCategory, Category category, Label label) {
        this.reviewer = reviewer;
        this.processor = processor;
        this.dueDate = dueDate;
        this.category.updateMainCategory(mainCategory);
        this.category = category;
        this.label = label;
    }

    private static String toTaskCode(Category category){
        return category.getMainCategory().getCode() + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHmm"));
    }
}
