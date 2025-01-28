package clap.server.adapter.outbound.persistense.entity.task;

import clap.server.adapter.outbound.persistense.entity.member.MemberEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Embeddable
@AllArgsConstructor
@Builder
public class TaskModificationInfo {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id")
    private TaskEntity task;

    @Column(name = "modified_field")
    private String modifiedField; //TODO: 속성 필요성 검토

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "modified_member_id")
    private MemberEntity modifiedMember;

    @Column(name = "new_value")
    private String newValue; //TODO: 속성 필요성 검토
}
