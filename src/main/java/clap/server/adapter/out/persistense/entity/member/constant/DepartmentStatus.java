package clap.server.adapter.out.persistense.entity.member.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DepartmentStatus {
    ACTIVE("활성"),
    INACTIVE("비활성");

    private final String description;
}
