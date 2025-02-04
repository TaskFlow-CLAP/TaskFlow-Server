package clap.server.application.service.member;

import clap.server.adapter.inbound.web.dto.member.request.UpdateMemberInfoRequest;
import clap.server.application.port.inbound.domain.MemberService;
import clap.server.application.port.inbound.member.UpdateMemberInfoUsecase;
import clap.server.application.port.outbound.s3.S3UploadPort;
import clap.server.common.annotation.architecture.ApplicationService;
import clap.server.domain.policy.attachment.FilePathPolicy;
import clap.server.common.utils.FileTypeValidator;
import clap.server.domain.model.member.Member;
import clap.server.exception.ApplicationException;
import clap.server.exception.code.FileErrorcode;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@ApplicationService
@RequiredArgsConstructor
@Transactional
class UpdateMemberInfoService implements UpdateMemberInfoUsecase {
    private final MemberService memberService;
    private final S3UploadPort s3UploadPort;

    @Override
    public void updateMemberInfo(Long memberId, UpdateMemberInfoRequest request, MultipartFile profileImage) throws IOException {
        if (!FileTypeValidator.validImageFile(profileImage.getInputStream())) {
            throw new ApplicationException(FileErrorcode.UNSUPPORTED_FILE_TYPE);
        }
        Member member = memberService.findActiveMember(memberId);
        String profileImageUrl = s3UploadPort.uploadSingleFile(FilePathPolicy.MEMBER_IMAGE, profileImage);
        member.updateMemberInfo(request.name(), request.agitNotification(), request.emailNotification(),
                request.kakaoWorkNotification(), profileImageUrl);
    }
}
