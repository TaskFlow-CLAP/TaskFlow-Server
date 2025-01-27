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
    private String modifiedField; //처리자(requestor) / 요청상태(taskStatus) -> task 상태 변경 혹은 처리 변경시에 taskhistory 테이블도 변경해야함

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "modified_member_id") //처리자가 바뀌는 경우
    private MemberEntity modifiedMember;

    @Column(name = "new_value")
    private String newValue; //상태가 바뀌는 경우
}
