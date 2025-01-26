package clap.server.adapter.inbound.web.admin;

import clap.server.adapter.inbound.security.SecurityUserDetails;
import clap.server.adapter.inbound.web.dto.admin.UpdateMemberInfoRequest;
import clap.server.application.port.inbound.admin.ManageMemberUsecase;
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

@Tag(name = "회원 관리 - 수정/ 삭제")
@WebAdapter
@RequiredArgsConstructor
@RequestMapping("/api/managements/members")
public class ManageMemberController {
    private final ManageMemberUsecase manageMemberUsecase;

    @Operation(summary = "단일 회원 등록 API")
    @PostMapping("/{memberId}")
    @Secured("ROLE_ADMIN")
    public void registerMember(@AuthenticationPrincipal SecurityUserDetails userInfo,
                               @PathVariable Long memberId,
                               @RequestBody @Valid UpdateMemberInfoRequest request){
        manageMemberUsecase.updateMemberInfo(memberId, request);
    }
}
