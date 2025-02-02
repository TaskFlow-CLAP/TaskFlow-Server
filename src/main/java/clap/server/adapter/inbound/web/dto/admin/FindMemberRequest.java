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
    private String nickName;

    @Schema(description = "부서 이름", example = "1")
    private String departmentName;

    @Schema(description = "회원 역할", example = "ROLE_USER")
    private MemberRole role;
}
