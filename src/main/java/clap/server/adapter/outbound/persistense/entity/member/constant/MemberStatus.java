package clap.server.adapter.outbound.persistense.entity.member.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberStatus {
    PENDING("회원 등록 대기중"),
    APPROVAL_REQUEST("회원 등록 요청 상태"),
    ACTIVE("활성"),
    INACTIVE("비활성"),
    DELETED("삭제");

    private final String description;
}
