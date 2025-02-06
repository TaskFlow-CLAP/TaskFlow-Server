package clap.server.adapter.outbound.persistense.entity.task;

import clap.server.adapter.outbound.persistense.entity.common.BaseTimeEntity;
import clap.server.adapter.outbound.persistense.entity.task.constant.TaskHistoryType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "task_history")
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLRestriction("is_deleted = false")
public class TaskHistoryEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_history_id")
    private Long taskHistoryId;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private TaskHistoryType type;

    @Embedded
    private TaskModificationInfo taskModificationInfo;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private CommentEntity comment;

    @Column(name="is_deleted", nullable = false)
    @Builder.Default
    private boolean isDeleted = Boolean.FALSE;
}
