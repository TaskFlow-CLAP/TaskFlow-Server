package clap.server.adapter.outbound.persistense.repository.notification;

import clap.server.adapter.outbound.persistense.entity.member.MemberEntity;
import clap.server.adapter.outbound.persistense.entity.notification.NotificationEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {

    @Query("SELECT n FROM NotificationEntity n " +
            "WHERE n.receiver.memberId = :receiverId " +
            "AND n.task.isDeleted = false " +
            "ORDER BY n.createdAt DESC")
    Slice<NotificationEntity> findAllByReceiver_MemberIdOrderByCreatedAtDesc(
            @Param("receiverId") Long receiverId, Pageable pageable);

    @Query("SELECT n FROM NotificationEntity n " +
            "WHERE n.receiver.memberId = :receiverId " +
            "AND n.task.isDeleted = false")
    List<NotificationEntity> findAllByReceiver_MemberId(Long receiverId);

    List<NotificationEntity> findByTask_TaskId(Long taskId);

    Integer countByIsReadFalseAndReceiver_MemberId(Long memberId);
}