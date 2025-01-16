package clap.server.adapter.out.persistense.entity.member;

import clap.server.adapter.out.persistense.entity.common.BaseTimeEntity;
import clap.server.adapter.out.persistense.entity.notification.NotificationChannelEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "member_notification")
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberNotificationEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long memberNotificationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private MemberEntity member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notification_channel_id")
    private NotificationChannelEntity notificationChannel;

    @Column(nullable = false)
    private boolean isEnabled;
}
