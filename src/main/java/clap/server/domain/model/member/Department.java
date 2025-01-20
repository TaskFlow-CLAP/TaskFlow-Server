package clap.server.domain.model.member;

import clap.server.adapter.outbound.persistense.entity.member.constant.DepartmentStatus;
import clap.server.domain.model.common.BaseTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Department extends BaseTime {
    private Long departmentId;
    private Long adminId;
    private String code;
    private String name;
    private DepartmentStatus status;
}
