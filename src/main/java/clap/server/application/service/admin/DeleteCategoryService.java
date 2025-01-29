package clap.server.application.service.admin;

import clap.server.application.port.inbound.admin.DeleteCategoryUsecase;
import clap.server.application.port.outbound.member.LoadMemberPort;
import clap.server.application.port.outbound.task.LoadCategoryPort;
import clap.server.common.annotation.architecture.ApplicationService;
import clap.server.domain.model.member.Member;
import clap.server.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import static clap.server.exception.code.MemberErrorCode.ACTIVE_MEMBER_NOT_FOUND;
import static clap.server.exception.code.TaskErrorCode.CATEGORY_NOT_FOUND;

@ApplicationService
@RequiredArgsConstructor
public class DeleteCategoryService implements DeleteCategoryUsecase {
    private final LoadCategoryPort loadCategoryPort;
    private final LoadMemberPort loadMemberPort;

    @Override
    @Transactional
    public void deleteCategory(Long adminId, Long categoryId) {
        Member admin = loadMemberPort.findActiveMemberById(adminId).orElseThrow(() -> new ApplicationException(ACTIVE_MEMBER_NOT_FOUND));
        loadCategoryPort.findById(categoryId)
                .orElseThrow(() -> new ApplicationException(CATEGORY_NOT_FOUND))
                .deleteCategory(admin);
    }
}
