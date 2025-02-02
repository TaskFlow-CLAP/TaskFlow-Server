package clap.server.adapter.outbound.persistense.repository.notification;

import clap.server.adapter.outbound.persistense.entity.member.MemberEntity;
import clap.server.adapter.outbound.persistense.entity.notification.NotificationEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {

    Slice<NotificationEntity> findAllByReceiver_MemberIdOrderByCreatedAtDesc(Long receiverId, Pageable pageable);

    List<NotificationEntity> findAllByReceiver_MemberId(Long memberId);

    Integer countByIsReadFalseAndReceiver_MemberId(Long memberId);
}