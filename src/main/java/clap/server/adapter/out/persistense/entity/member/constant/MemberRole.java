package clap.server.adapter.out.persistense.entity.member.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberRole {
    ROLE_ADMIN("관리자"),
    ROLE_USER("사용자"),
    ROLE_MANAGER("담당자");

    private final String description;
}