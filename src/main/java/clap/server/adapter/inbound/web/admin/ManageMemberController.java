package clap.server.adapter.inbound.web.admin;

import clap.server.adapter.inbound.security.SecurityUserDetails;
import clap.server.adapter.inbound.web.dto.admin.UpdateMemberRequest;
import clap.server.application.port.inbound.admin.UpdateMemberUsecase;
import clap.server.common.annotation.architecture.WebAdapter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "05. Admin")
@WebAdapter
@RequiredArgsConstructor
@RequestMapping("/api/managements/members")
public class ManageMemberController {
    private final UpdateMemberUsecase updateMemberUsecase;

    @Operation(summary = "회원 정보 수정 API")
    @PostMapping("/{memberId}")
    @Secured("ROLE_ADMIN")
    public void registerMember(@AuthenticationPrincipal SecurityUserDetails userInfo,
                               @PathVariable Long memberId,
                               @RequestBody @Valid UpdateMemberRequest request){
        updateMemberUsecase.updateMemberInfo(userInfo.getUserId(), memberId, request);
    }
}
