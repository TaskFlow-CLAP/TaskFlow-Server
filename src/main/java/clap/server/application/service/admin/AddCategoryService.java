package clap.server.application.service.admin;

import clap.server.application.port.inbound.admin.AddMainCategoryUsecase;
import clap.server.application.port.inbound.admin.AddSubCategoryUsecase;
import clap.server.application.port.outbound.member.LoadMemberPort;
import clap.server.application.port.outbound.task.CommandCategoryPort;
import clap.server.application.port.outbound.task.LoadCategoryPort;
import clap.server.common.annotation.architecture.ApplicationService;
import clap.server.domain.model.member.Member;
import clap.server.domain.model.task.Category;
import clap.server.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static clap.server.exception.code.MemberErrorCode.ACTIVE_MEMBER_NOT_FOUND;
import static clap.server.exception.code.TaskErrorCode.CATEGORY_NOT_FOUND;

@ApplicationService
@RequiredArgsConstructor
public class AddCategoryService implements AddMainCategoryUsecase, AddSubCategoryUsecase {
    private final CommandCategoryPort commandCategoryPort;
    private final LoadCategoryPort loadCategoryPort;
    private final LoadMemberPort loadMemberPort;

    @Override
    @Transactional
    public void addMainCategory(Long adminId, String code, String name) {
        Optional<Member> activeMember = loadMemberPort.findActiveMemberById(adminId);
        Category mainCategory = Category.createMainCategory(
                activeMember.orElseThrow(() -> new ApplicationException(ACTIVE_MEMBER_NOT_FOUND)),
                code, name);
        commandCategoryPort.save(mainCategory);
    }

    @Override
    @Transactional
    public void addSubCategory(Long adminId, Long mainCategoryId, String code, String name, String descriptionExample) {
        Optional<Member> activeMember = loadMemberPort.findActiveMemberById(adminId);
        Optional<Category> mainCategory = loadCategoryPort.findById(mainCategoryId);

        Category subCategory = Category.createSubCategory(
                activeMember.orElseThrow(() -> new ApplicationException(ACTIVE_MEMBER_NOT_FOUND)),
                mainCategory.orElseThrow(() -> new ApplicationException(CATEGORY_NOT_FOUND)),
                code, name, descriptionExample);
        commandCategoryPort.save(subCategory);
    }
}