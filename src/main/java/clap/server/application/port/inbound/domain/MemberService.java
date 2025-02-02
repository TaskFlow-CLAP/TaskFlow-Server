package clap.server.application.port.inbound.domain;

import clap.server.application.port.outbound.member.CommandMemberPort;
import clap.server.application.port.outbound.member.LoadMemberPort;
import clap.server.domain.model.member.Member;
import clap.server.domain.model.task.Task;
import clap.server.adapter.outbound.persistense.entity.task.constant.TaskStatus;
import clap.server.exception.ApplicationException;
import clap.server.exception.ApplicationException;
import clap.server.exception.code.MemberErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final LoadMemberPort loadMemberPort;
    private final CommandMemberPort commandMemberPort;


    public Member findById(Long memberId) {
        return loadMemberPort.findById(memberId).orElseThrow(
                () -> new ApplicationException(MemberErrorCode.MEMBER_NOT_FOUND));
    }

    public Member findActiveMember(Long memberId) {
        return loadMemberPort.findActiveMemberById(memberId).orElseThrow(
                () -> new ApplicationException(MemberErrorCode.ACTIVE_MEMBER_NOT_FOUND));
    }

    public int getRemainingTasks(Long memberId) {
        List<TaskStatus> targetStatuses = List.of(TaskStatus.IN_PROGRESS, TaskStatus.PENDING_COMPLETED);
        return loadMemberPort.findTasksByMemberIdAndStatus(memberId, targetStatuses).size();
    }

    public String getMemberNickname(Long memberId) {
        Member member = findById(memberId);
        if (member.getMemberInfo() == null) {
            throw new ApplicationException(MemberErrorCode.MEMBER_NOT_FOUND);
        }
        return member.getMemberInfo().getNickname();
    }

    public String getMemberImageUrl(Long memberId) {
        Member member = findById(memberId);
        return member.getImageUrl() != null ? member.getImageUrl() : "default-image-url";
    }

    public List<Member> findActiveManagers() {
        List<Member> activeManagers = loadMemberPort.findActiveManagers();

        if (activeManagers.isEmpty()) {
            return List.of();
        }
        return activeManagers;
    }

    public Member findReviewer(Long memberId) {
        return loadMemberPort.findReviewerById(memberId).orElseThrow(
                ()-> new ApplicationException(MemberErrorCode.NOT_A_REVIEWER)
        );
    }

    public List<Member> findReviewers() {
        return loadMemberPort.findReviewers();
    }

}
