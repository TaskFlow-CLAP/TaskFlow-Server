package clap.server.domain.model.task;

import clap.server.adapter.outbound.persistense.entity.task.constant.TaskHistoryType;
import clap.server.domain.model.common.BaseTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TaskHistory extends BaseTime {
    private Long historyId;
    private TaskHistoryType type;
    private TaskModificationInfo taskModificationInfo;
    private Comment comment;

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class TaskModificationInfo {
        private Long taskId;
        private String modifiedField;
        private Long modifiedMemberId;
        private String newValue;

        @Builder
        public TaskModificationInfo(Long taskId, String modifiedField, Long modifiedMemberId, String newValue) {
            this.taskId = taskId;
            this.modifiedField = modifiedField;
            this.modifiedMemberId = modifiedMemberId;
            this.newValue = newValue;
        }
    }
}

