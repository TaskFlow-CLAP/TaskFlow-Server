package clap.server.application.port.inbound.domain;

import clap.server.application.port.outbound.member.LoadMemberPort;
import clap.server.domain.model.member.Member;
import clap.server.adapter.outbound.persistense.entity.task.constant.TaskStatus;
import clap.server.exception.ApplicationException;
import clap.server.exception.code.MemberErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final LoadMemberPort loadMemberPort;

    public Member findById(Long memberId) {
        return loadMemberPort.findById(memberId).orElseThrow(
                () -> new ApplicationException(MemberErrorCode.MEMBER_NOT_FOUND));
    }

    public Member findActiveMember(Long memberId) {
        return loadMemberPort.findActiveMemberById(memberId).orElseThrow(
                () -> new ApplicationException(MemberErrorCode.ACTIVE_MEMBER_NOT_FOUND));
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
