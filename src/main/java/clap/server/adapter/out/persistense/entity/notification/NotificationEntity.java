package clap.server.adapter.out.persistense.entity.notification;

import clap.server.adapter.out.persistense.entity.common.BaseTimeEntity;
import clap.server.adapter.out.persistense.entity.member.MemberEntity;
import clap.server.adapter.out.persistense.entity.notification.constant.NotificationPublisherType;
import clap.server.adapter.out.persistense.entity.task.TaskEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@Entity
@Table(name = "notification")
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NotificationEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long notificationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id")
    private TaskEntity task;

    @Column(nullable = false)
    private String type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", nullable = false)
    private MemberEntity receiver;

    @Column(nullable = false)
    private String message;

    @Column(name = "publisher_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private NotificationPublisherType publisherType;

    @Column(nullable = false)
    private boolean isRead;
}
