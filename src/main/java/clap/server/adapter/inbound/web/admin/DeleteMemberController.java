package clap.server.adapter.inbound.web.admin;

import clap.server.application.port.inbound.admin.DeleteMemberUsecase;
import clap.server.common.annotation.architecture.WebAdapter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "05. Admin")
@WebAdapter
@RequiredArgsConstructor
@RequestMapping("/api/managements")
public class DeleteMemberController {
    private final DeleteMemberUsecase deleteMemberUsecase;

    @Operation(summary = "회원 삭제 API")
    @Secured("ROLE_ADMIN")
    @PatchMapping("/members/{memberId}")
    public void deleteMember(@PathVariable Long memberId) {
        deleteMemberUsecase.deleteMember(memberId);
    }
}
