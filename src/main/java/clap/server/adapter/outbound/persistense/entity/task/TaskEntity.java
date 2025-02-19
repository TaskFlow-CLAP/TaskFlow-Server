package clap.server.adapter.outbound.persistense.entity.task;

import clap.server.adapter.outbound.persistense.entity.common.BaseTimeEntity;
import clap.server.adapter.outbound.persistense.entity.member.MemberEntity;
import clap.server.adapter.outbound.persistense.entity.task.constant.TaskStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

@Entity
@Table(name = "task")
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLRestriction("is_deleted = false")
public class TaskEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long taskId;

    @Column(nullable = false)
    private String taskCode;

    @Column(nullable = false)
    private String title;

    @Column(length = 1000)
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", nullable = false)
    private CategoryEntity category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_id", nullable = false)
    private MemberEntity requester;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TaskStatus taskStatus;

    @Column
    private Long processorOrder;

    @Column
    private Long agitPostId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewer_id")
    private MemberEntity reviewer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "processor_id")
    private MemberEntity processor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "label_id")
    private LabelEntity label;

    @Column
    private LocalDateTime dueDate;

    @Column
    private LocalDateTime finishedAt;

    @Column(nullable = false)
    private int attachmentCount;

    @Column(name="is_deleted", nullable = false)
    @Builder.Default
    private boolean isDeleted = Boolean.FALSE;
}
