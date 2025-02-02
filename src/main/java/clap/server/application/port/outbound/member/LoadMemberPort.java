package clap.server.application.port.outbound.member;

import clap.server.adapter.inbound.web.dto.admin.FindMemberRequest;
import clap.server.domain.model.member.Member;
import clap.server.domain.model.task.Task;  
import clap.server.adapter.outbound.persistense.entity.task.constant.TaskStatus; 
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface LoadMemberPort {
    Optional<Member> findById(Long id);

    Optional<Member> findActiveMemberById(Long id);

    Optional<Member> findByNickname(String nickname);

    List<Member> findReviewers();

    Page<Member> findAllMembers(Pageable pageable);

    Page<Member> findMembersWithFilter(Pageable pageable, FindMemberRequest filterRequest);

    Optional<Member> findReviewerById(Long id);

}
