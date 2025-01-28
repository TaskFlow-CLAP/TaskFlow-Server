package clap.server.adapter.inbound.web.member;

import clap.server.adapter.inbound.security.SecurityUserDetails;
import clap.server.adapter.inbound.web.dto.member.MemberProfileResponse;
import clap.server.application.port.inbound.member.MemberInfoUsecase;
import clap.server.common.annotation.architecture.WebAdapter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "01. Member", description = "회원 정보 조회 및 수정 API")
@WebAdapter
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberInfoController {
    private final MemberInfoUsecase memberInfoUsecase;

    @Operation(summary = "회원 프로필을 조회합니다. 활성화된 회원만 조회 가능합니다.")
    @GetMapping("/profile")
    public ResponseEntity<MemberProfileResponse> getMemberProfile(@AuthenticationPrincipal SecurityUserDetails userInfo) {
        return ResponseEntity.ok(memberInfoUsecase.getMemberProfile(userInfo.getUserId()));
    }

}
