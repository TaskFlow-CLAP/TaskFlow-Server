package clap.server.adapter.inbound.web.admin;

import clap.server.adapter.inbound.security.service.SecurityUserDetails;
import clap.server.application.port.inbound.admin.DeleteCategoryUsecase;
import clap.server.common.annotation.architecture.WebAdapter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "05. Admin [작업 관리]")
@WebAdapter
@RequiredArgsConstructor
@RequestMapping("/api/managements")
public class DeleteCategoryController {
    private final DeleteCategoryUsecase deleteCategoryUsecase;

    @Operation(summary = "카테고리 삭제")
    @DeleteMapping("/categories/{categoryId}")
    @Secured("ROLE_ADMIN")
    public void deleteCategory(@AuthenticationPrincipal SecurityUserDetails userInfo, @PathVariable Long categoryId) {
        deleteCategoryUsecase.deleteCategory(userInfo.getUserId(), categoryId);
    }
}
