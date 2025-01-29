package clap.server.domain.model.task;

import clap.server.adapter.inbound.web.dto.label.AddLabelRequest;
import clap.server.adapter.outbound.persistense.entity.task.constant.LabelColor;
import clap.server.adapter.outbound.persistense.entity.task.constant.TaskStatus;
import clap.server.domain.model.common.BaseTime;
import clap.server.domain.model.member.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Label extends BaseTime {
    private Long labelId;
    private Member admin;
    private String labelName;
    private LabelColor labelColor;
    private boolean isDeleted;

    public static Label addLabel(Member admin, AddLabelRequest request) {
        return Label.builder()
                .admin(admin)
                .labelName(request.labelName())
                .labelColor(request.labelColor())
                .isDeleted(false)
                .build();
    }
}
