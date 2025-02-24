package clap.server.application.port.inbound.domain;

import clap.server.application.port.outbound.member.LoadMemberPort;
import clap.server.domain.model.member.Member;
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

    public Member findActiveMemberWithDepartment(Long memberId) {
        return loadMemberPort.findActiveMemberByIdWithFetchDepartment(memberId).orElseThrow(
                () -> new ApplicationException(MemberErrorCode.MEMBER_NOT_FOUND));
    }

    public Member findMemberWithDepartment(Long memberId) {
        return loadMemberPort.findByIdWithFetchDepartment(memberId).orElseThrow(
                () -> new ApplicationException(MemberErrorCode.MEMBER_NOT_FOUND));
    }

}
