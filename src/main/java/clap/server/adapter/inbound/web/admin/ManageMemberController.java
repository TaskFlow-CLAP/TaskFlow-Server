package clap.server.adapter.inbound.web.admin;

import clap.server.adapter.inbound.security.service.SecurityUserDetails;
import clap.server.adapter.inbound.web.dto.admin.request.UpdateMemberRequest;
import clap.server.adapter.inbound.web.dto.admin.response.MemberDetailsResponse;
import clap.server.application.port.inbound.admin.MemberDetailUsecase;
import clap.server.application.port.inbound.admin.UpdateMemberUsecase;
import clap.server.common.annotation.architecture.WebAdapter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "05. Admin [회원 관리]")
@WebAdapter
@RequiredArgsConstructor
@RequestMapping("/api/managements/members")
public class ManageMemberController {
    private final UpdateMemberUsecase updateMemberUsecase;
    private final MemberDetailUsecase memberDetailUsecase;

    @Operation(summary = "회원 정보 수정 API")
    @PatchMapping("/{memberId}")
    @Secured("ROLE_ADMIN")
    public void registerMember(@AuthenticationPrincipal SecurityUserDetails userInfo,
                               @PathVariable Long memberId,
                               @RequestBody @Valid UpdateMemberRequest request) {
        updateMemberUsecase.updateMemberInfo(userInfo.getUserId(), memberId, request);
    }

    @Operation(summary = "회원 상세 정보 조회 API")
    @GetMapping("/{memberId}/details")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<MemberDetailsResponse> getMemberDetail(@AuthenticationPrincipal SecurityUserDetails userInfo,
                                                                 @PathVariable Long memberId) {
        return ResponseEntity.ok(memberDetailUsecase.getMemberDetail(memberId));
    }
}
