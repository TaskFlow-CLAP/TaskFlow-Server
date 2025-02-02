package clap.server.adapter.inbound.web.dto.admin;

import clap.server.adapter.outbound.persistense.entity.member.constant.MemberRole;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class FindMemberRequest {
    @Schema(description = "회원 이름", example = "양시훈")
    private String name;

    @Schema(description = "회원 이메일", example = "sihun123@gmail.com")
    private String email;

    @Schema(description = "회원 닉네임", example = "leo.sh")
    private String nickname;

    @Schema(description = "부서 ID", example = "1")
    private Long departmentId;

    @Schema(description = "회원 역할", example = "ROLE_USER")
    private MemberRole role;
}
