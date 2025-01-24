package clap.server.domain.model.task;

import clap.server.adapter.outbound.persistense.entity.task.constant.TaskStatus;
import clap.server.domain.model.common.BaseTime;
import clap.server.domain.model.member.Member;
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
    private int processorOrder; //칸반보드 상태에 따른 순서
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
                .taskCode(category.getMainCategory().getCode() + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHmm")))
                .build();
    }

    public static Task updateTask(Task task, Member member, Category category, String title, String description) {

        return Task.builder()
                .taskId(task.getTaskId())
                .requester(member)
                .category(category)
                .title(title)
                .description(description)
                .taskStatus(TaskStatus.REQUESTED)
                .taskCode(category.getMainCategory().getCode() + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHmm")))
                .build();
    }

}
