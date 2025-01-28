package clap.server.domain.model.task;

import clap.server.adapter.outbound.persistense.entity.member.constant.MemberStatus;
import clap.server.adapter.outbound.persistense.entity.task.constant.TaskHistoryType;
import clap.server.domain.model.common.BaseTime;
import clap.server.domain.model.member.Member;
import clap.server.domain.model.member.MemberInfo;
import co.elastic.clients.elasticsearch.inference.TaskType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TaskHistory extends BaseTime {
    private Long taskHistoryId;
    private TaskHistoryType type;
    private TaskModificationInfo taskModificationInfo;
    private Comment comment;

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class TaskModificationInfo {
        private Task task;
        private String modifiedField;
        private Member modifiedMember;
        private String newValue;

        @Builder
        public TaskModificationInfo(Task task, String modifiedField, Member modifiedMember, String newValue) {
            this.task = task;
            this.modifiedField = modifiedField;
            this.modifiedMember = modifiedMember;
            this.newValue = newValue;
        }
    }
}

