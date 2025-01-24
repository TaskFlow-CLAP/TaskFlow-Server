package clap.server.application.service.admin;

import clap.server.application.port.inbound.management.AddMainCategoryUsecase;
import clap.server.application.port.outbound.member.LoadMemberPort;
import clap.server.application.port.outbound.task.CommandCategoryPort;
import clap.server.common.annotation.architecture.ApplicationService;
import clap.server.domain.model.member.Member;
import clap.server.domain.model.task.Category;
import clap.server.exception.ApplicationException;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static clap.server.exception.code.MemberErrorCode.ACTIVE_MEMBER_NOT_FOUND;

@ApplicationService
@RequiredArgsConstructor
public class AddMainCategoryService implements AddMainCategoryUsecase {
    private final CommandCategoryPort commandCategoryPort;
    private final LoadMemberPort loadMemberPort;

    @Override
    public void addMainCategory(Long adminId, String code, String name) {
        Optional<Member> activeMember = loadMemberPort.findActiveMemberById(adminId);
        Category mainCategory = Category.createMainCategory(
                activeMember.orElseThrow(() -> new ApplicationException(ACTIVE_MEMBER_NOT_FOUND)),
                code, name);
        commandCategoryPort.save(mainCategory);
    }
}