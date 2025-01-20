package clap.server.domain.model.task;

import clap.server.domain.model.common.BaseTime;
import clap.server.domain.model.member.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

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
    private Status status;
    private int processorOrder; //칸반보드 상태에 따른 순서
    private Member processor;
    private Label label;
    private Member reviewer;
    private LocalDateTime dueDate;
    private LocalDateTime completedAt;
}
