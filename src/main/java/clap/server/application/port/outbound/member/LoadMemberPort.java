package clap.server.application.port.outbound.member;

import clap.server.domain.model.member.Member;
import clap.server.domain.model.task.Task;  // Task 클래스 임포트 확인
import clap.server.adapter.outbound.persistense.entity.task.constant.TaskStatus; // TaskStatus 임포트
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface LoadMemberPort {
    Optional<Member> findById(Long id);

    Optional<Member> findActiveMemberById(Long id);

    List<Member> findActiveManagers();

    List<Task> findTasksByMemberIdAndStatus(Long memberId, List<TaskStatus> taskStatuses);

    int getRemainingTasks(Long memberId);

    Optional<Member> findByNickname(String nickname);

    List<Member> findReviewers();

    Page<Member> findAllMembers(Pageable pageable);

}
