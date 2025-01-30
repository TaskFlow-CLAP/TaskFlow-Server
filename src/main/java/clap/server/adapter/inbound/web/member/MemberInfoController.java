package clap.server.adapter.inbound.web.member;

import clap.server.adapter.inbound.security.SecurityUserDetails;
import clap.server.adapter.inbound.web.dto.member.MemberDetailInfoResponse;
import clap.server.adapter.inbound.web.dto.member.MemberProfileResponse;
import clap.server.adapter.inbound.web.dto.member.UpdateMemberInfoRequest;
import clap.server.application.port.inbound.member.UpdateMemberInfoUsecase;
import clap.server.application.port.inbound.member.MemberDetailInfoUsecase;
import clap.server.application.port.inbound.member.MemberProfileUsecase;
import clap.server.common.annotation.architecture.WebAdapter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "01. Member", description = "회원 정보 조회 및 수정 API")
@WebAdapter
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberInfoController {
    private final MemberProfileUsecase memberProfileUsecase;
    private final MemberDetailInfoUsecase memberDetailInfoUsecase;
    private final UpdateMemberInfoUsecase updateMemberInfoUsecase;

    @Operation(summary = "회원 프로필 조회 API")
    @GetMapping("/profile")
    public ResponseEntity<MemberProfileResponse> getMemberProfile(@AuthenticationPrincipal SecurityUserDetails userInfo) {
        return ResponseEntity.ok(memberProfileUsecase.getMemberProfile(userInfo.getUserId()));
    }

    @Operation(summary = "회원 프로필 조회 API")
    @GetMapping("/info")
    public ResponseEntity<MemberDetailInfoResponse> getMemberDetailInfo(@AuthenticationPrincipal SecurityUserDetails userInfo) {
        return ResponseEntity.ok(memberDetailInfoUsecase.getMemberInfo(userInfo.getUserId()));
    }
}
