package clap.server.domain.model.task;

import clap.server.adapter.outbound.persistense.entity.task.constant.TaskHistoryType;
import clap.server.domain.model.common.BaseTime;
import clap.server.domain.model.member.Member;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TaskHistory extends BaseTime {
    private Long taskHistoryId;
    private TaskHistoryType type;
    private TaskModificationInfo taskModificationInfo;
    private Member modifiedMember;
    private Comment comment;

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class TaskModificationInfo {
        private Task task;
        private String modifiedStatus;
    }

    public static TaskHistory createTaskHistory(TaskHistoryType type, Task task, String statusDescription, Member member) {
        return TaskHistory.builder()
                .type(type)
                .modifiedMember(member)
                .taskModificationInfo(
                        TaskModificationInfo.builder()
                                .task(task)
                                .modifiedStatus(statusDescription)
                                .build()
                )
                .comment(null)
                .build();
    }

    public static TaskHistory createCommentTaskHistory(TaskHistoryType type, Member member, Comment comment) {
        return TaskHistory.builder()
                .type(type)
                .modifiedMember(member)
                .taskModificationInfo(
                        TaskModificationInfo.builder()
                                .task(null)
                                .modifiedStatus(null)
                                .build()
                )
                .comment(comment)
                .build();
    }
}

