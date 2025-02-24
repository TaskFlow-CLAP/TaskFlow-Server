package clap.server.application.service.member;

import clap.server.adapter.inbound.web.dto.member.request.UpdateMemberInfoRequest;
import clap.server.application.port.inbound.domain.MemberService;
import clap.server.application.port.inbound.member.UpdateMemberInfoUsecase;
import clap.server.application.port.outbound.member.CommandMemberPort;
import clap.server.application.port.outbound.member.LoadMemberPort;
import clap.server.application.port.outbound.s3.S3UploadPort;
import clap.server.common.annotation.architecture.ApplicationService;
import clap.server.domain.model.member.Member;
import clap.server.common.constants.FilePathConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@ApplicationService
@RequiredArgsConstructor
@Transactional
class UpdateMemberInfoService implements UpdateMemberInfoUsecase {
    private final MemberService memberService;
    private final S3UploadPort s3UploadPort;
    private final CommandMemberPort commandMemberPort;

    @Override
    public void updateMemberInfo(Long memberId, UpdateMemberInfoRequest request, MultipartFile profileImage) {
        Member member = memberService.findActiveMemberWithDepartment(memberId);
        if(request.isProfileImageDeleted()){
            member.setImageUrl(null);
        }
        else {
            String profileImageUrl = profileImage != null ? s3UploadPort.uploadSingleFile(FilePathConstants.MEMBER_IMAGE, profileImage) : member.getImageUrl();
            member.setImageUrl(profileImageUrl);
        }
        member.updateMemberInfo(request.name(), request.emailNotification(),
                request.kakaoWorkNotification());
        commandMemberPort.save(member);
    }
}
