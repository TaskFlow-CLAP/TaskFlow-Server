package clap.server.adapter.inbound.web.member;

import clap.server.adapter.inbound.security.service.SecurityUserDetails;
import clap.server.adapter.inbound.web.dto.member.response.MemberDetailInfoResponse;
import clap.server.adapter.inbound.web.dto.member.response.MemberProfileResponse;
import clap.server.adapter.inbound.web.dto.member.request.UpdateMemberInfoRequest;
import clap.server.application.port.inbound.member.UpdateMemberInfoUsecase;
import clap.server.application.port.inbound.member.MemberDetailInfoUsecase;
import clap.server.application.port.inbound.member.MemberProfileUsecase;
import clap.server.common.annotation.architecture.WebAdapter;
import clap.server.common.utils.FileTypeValidator;
import clap.server.exception.ApplicationException;
import clap.server.exception.code.FileErrorcode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Tag(name = "01. Member [회원 정보]", description = "회원 정보 조회 및 수정 API")
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

    @Operation(summary = "회원 상세 정보 조회 API")
    @GetMapping("/info")
    public ResponseEntity<MemberDetailInfoResponse> getMemberDetailInfo(@AuthenticationPrincipal SecurityUserDetails userInfo) {
        return ResponseEntity.ok(memberDetailInfoUsecase.getMemberInfo(userInfo.getUserId()));
    }

    @Operation(summary = "회원 정보 수정 API", description = "이미지 수정이 없을 시에는 profileImage를 보내지 않습니다.")
    @PatchMapping(value = "/info", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public void updateMemberDetailInfo(
            @RequestPart(name = "memberInfo") UpdateMemberInfoRequest request,
            @Parameter(description = "image/jpeg, image/pjpeg, image/png, image/gif, image/bmp, image/x-windows-bmp 형식만 가능합니다.", content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE))
            @RequestPart(name = "profileImage", required = false) MultipartFile profileImage,
            @AuthenticationPrincipal SecurityUserDetails userInfo) throws IOException {
        if (profileImage !=null && !FileTypeValidator.validImageFile(profileImage.getInputStream())) {
            throw new ApplicationException(FileErrorcode.UNSUPPORTED_FILE_TYPE);
        }
        updateMemberInfoUsecase.updateMemberInfo(userInfo.getUserId(), request, profileImage);
    }
}
