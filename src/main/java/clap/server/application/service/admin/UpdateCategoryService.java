package clap.server.application.service.admin;

import clap.server.application.port.inbound.admin.UpdateCategoryUsecase;
import clap.server.application.port.outbound.member.LoadMemberPort;
import clap.server.application.port.outbound.task.CommandCategoryPort;
import clap.server.application.port.outbound.task.LoadCategoryPort;
import clap.server.common.annotation.architecture.ApplicationService;
import clap.server.domain.model.member.Member;
import clap.server.domain.model.task.Category;
import clap.server.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import static clap.server.exception.code.MemberErrorCode.ACTIVE_MEMBER_NOT_FOUND;
import static clap.server.exception.code.TaskErrorCode.CATEGORY_DUPLICATE;
import static clap.server.exception.code.TaskErrorCode.CATEGORY_NOT_FOUND;

@ApplicationService
@RequiredArgsConstructor
public class UpdateCategoryService implements UpdateCategoryUsecase {
    private final LoadCategoryPort loadCategoryPort;
    private final LoadMemberPort loadMemberPort;
    private final CommandCategoryPort commandCategoryPort;

    @Override
    @Transactional
    public void updateCategory(Long adminId, Long categoryId, String name, String code, String descriptionExample) {
        Member admin = loadMemberPort.findActiveMemberById(adminId).orElseThrow(() -> new ApplicationException(ACTIVE_MEMBER_NOT_FOUND));
        Category category = loadCategoryPort.findById(categoryId).orElseThrow(() -> new ApplicationException(CATEGORY_NOT_FOUND));
        boolean isDuplicate;
        if (category.getMainCategory() == null) {
            isDuplicate = loadCategoryPort.existsMainCategoryByNameOrCode(category, name, code);
        } else {
            isDuplicate = loadCategoryPort.existsSubCategoryByNameOrCode(category, category.getMainCategory(), name, code);
        }
        if (isDuplicate) throw new ApplicationException(CATEGORY_DUPLICATE);

        category.updateCategory(admin, name, code, descriptionExample);
        commandCategoryPort.save(category);
    }
}