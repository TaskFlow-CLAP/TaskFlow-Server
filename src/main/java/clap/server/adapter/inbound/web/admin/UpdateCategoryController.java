package clap.server.adapter.inbound.web.admin;

import clap.server.adapter.inbound.security.service.SecurityUserDetails;
import clap.server.adapter.inbound.web.dto.admin.request.UpdateCategoryRequest;
import clap.server.application.port.inbound.admin.UpdateCategoryUsecase;
import clap.server.common.annotation.architecture.WebAdapter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "05. Admin [작업 관리]")
@WebAdapter
@RequiredArgsConstructor
@RequestMapping("/api/managements")
public class UpdateCategoryController {
    private final UpdateCategoryUsecase updateCategoryUsecase;

    @Operation(summary = "카테고리 수정")
    @PatchMapping("/categories/{categoryId}")
    @Secured("ROLE_ADMIN")
    public void updateCategory(@AuthenticationPrincipal SecurityUserDetails userInfo, @PathVariable Long categoryId,
                               @Valid @RequestBody UpdateCategoryRequest updateCategoryRequest) {
        updateCategoryUsecase.updateCategory(userInfo.getUserId(),
                categoryId,
                updateCategoryRequest.name(),
                updateCategoryRequest.code(),
                updateCategoryRequest.descriptionExample());
    }
}