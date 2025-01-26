package clap.server.adapter.inbound.web.admin;

import clap.server.adapter.inbound.security.SecurityUserDetails;
import clap.server.adapter.inbound.web.dto.admin.UpdateCategoryRequest;
import clap.server.application.port.inbound.admin.UpdateCategoryUsecase;
import clap.server.common.annotation.architecture.WebAdapter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "카테고리 수정")
@WebAdapter
@RequiredArgsConstructor
@RequestMapping("/api")
public class UpdateCategoryController {
    private final UpdateCategoryUsecase updateCategoryUsecase;

    @Operation(summary = "카테고리 수정")
    @PatchMapping("/category")
    @Secured("ROLE_ADMIN")
    public void updateCategory(@AuthenticationPrincipal SecurityUserDetails userInfo, UpdateCategoryRequest updateCategoryRequest) {
        updateCategoryUsecase.updateCategory(userInfo.getUserId(), updateCategoryRequest.categoryId(), updateCategoryRequest.name(), updateCategoryRequest.code());
    }
}