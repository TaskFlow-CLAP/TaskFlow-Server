package clap.server.adapter.inbound.web.admin;

import clap.server.adapter.inbound.security.SecurityUserDetails;
import clap.server.adapter.inbound.web.dto.AddSubCategoryRequest;
import clap.server.adapter.inbound.web.dto.admin.AddMainCategoryRequest;
import clap.server.application.port.inbound.management.AddMainCategoryUsecase;
import clap.server.application.port.inbound.management.AddSubCategoryUsecase;
import clap.server.common.annotation.architecture.WebAdapter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "카테고리 추가")
@WebAdapter
@RequiredArgsConstructor
public class addCategoryController {
    private final AddMainCategoryUsecase addMainCategoryUsecase;
    private final AddSubCategoryUsecase addSubCategoryUsecase;

    @Operation(summary = "1차 카테고리 추가")
    @PostMapping("/api/maincategory")
    @Secured("ROLE_ADMIN")
    public void addMainCategory(@AuthenticationPrincipal SecurityUserDetails userInfo, @Valid @RequestBody AddMainCategoryRequest addMainCategoryRequest) {
        addMainCategoryUsecase.addMainCategory(userInfo.getUserId(), addMainCategoryRequest.code(), addMainCategoryRequest.name());
    }

    @Operation(summary = "2차 카테고리 추가")
    @PostMapping("/api/subcategory")
    @Secured("ROLE_ADMIN")
    public void addSubCategory(@AuthenticationPrincipal SecurityUserDetails userInfo, @Valid @RequestBody AddSubCategoryRequest addCategoryRequest) {
        addSubCategoryUsecase.addSubCategory(userInfo.getUserId(), addCategoryRequest.mainCategoryId(), addCategoryRequest.code(), addCategoryRequest.name());
    }

}