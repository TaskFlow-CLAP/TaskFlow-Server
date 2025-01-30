package clap.server.application.service.member;

import clap.server.adapter.inbound.web.dto.member.UpdateMemberInfoRequest;
import clap.server.application.port.inbound.domain.MemberService;
import clap.server.application.port.inbound.member.UpdateMemberInfoUsecase;
import clap.server.application.port.outbound.s3.S3UploadPort;
import clap.server.common.annotation.architecture.ApplicationService;
import clap.server.common.constants.FilePathConstants;
import clap.server.common.utils.FileUtils;
import clap.server.domain.model.member.Member;
import clap.server.exception.ApplicationException;
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
        Member member = memberService.findActiveMember(memberId);
        member.updateMemberInfo(request.name(), request.agitNotification(), request.emailNotification(),
                request.kakaoWorkNotification(), profileImageUrl);
    }
}
