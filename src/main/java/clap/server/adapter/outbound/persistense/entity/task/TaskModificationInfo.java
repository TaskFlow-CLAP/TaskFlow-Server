package clap.server.adapter.outbound.persistense.entity.task;

import clap.server.adapter.outbound.persistense.entity.member.MemberEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Embeddable
public class TaskModificationInfo {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id")
    private TaskEntity task;

    @Column(name = "modified_field")
    private String modifiedField;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "modified_member_id")
    private MemberEntity member;

    @Column(name = "new_value")
    private String newValue;
}
