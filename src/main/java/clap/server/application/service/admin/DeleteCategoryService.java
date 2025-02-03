package clap.server.application.service.admin;

import clap.server.application.port.inbound.admin.DeleteCategoryUsecase;
import clap.server.application.port.inbound.domain.MemberService;
import clap.server.application.port.outbound.task.CommandCategoryPort;
import clap.server.application.port.outbound.task.LoadCategoryPort;
import clap.server.common.annotation.architecture.ApplicationService;
import clap.server.domain.model.member.Member;
import clap.server.domain.model.task.Category;
import clap.server.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import static clap.server.exception.code.TaskErrorCode.CATEGORY_NOT_FOUND;

@ApplicationService
@RequiredArgsConstructor
public class DeleteCategoryService implements DeleteCategoryUsecase {
    private final LoadCategoryPort loadCategoryPort;
    private final MemberService memberService;
    private final CommandCategoryPort commandCategoryPort;

    @Override
    @Transactional
    public void deleteCategory(Long adminId, Long categoryId) {
        Member admin = memberService.findActiveMember(adminId);
        Category category = loadCategoryPort.findById(categoryId)
                .orElseThrow(() -> new ApplicationException(CATEGORY_NOT_FOUND));
        category.deleteCategory(admin);
        commandCategoryPort.save(category);
    }
}
