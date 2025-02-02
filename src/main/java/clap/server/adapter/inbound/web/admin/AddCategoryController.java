package clap.server.adapter.inbound.web.admin;

import clap.server.adapter.inbound.security.SecurityUserDetails;
import clap.server.adapter.inbound.web.dto.admin.AddMainCategoryRequest;
import clap.server.adapter.inbound.web.dto.admin.AddSubCategoryRequest;
import clap.server.application.port.inbound.admin.AddMainCategoryUsecase;
import clap.server.application.port.inbound.admin.AddSubCategoryUsecase;
import clap.server.common.annotation.architecture.WebAdapter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "05. Admin")
@WebAdapter
@RequiredArgsConstructor
@RequestMapping("/api/managements")
public class AddCategoryController {
    private final AddMainCategoryUsecase addMainCategoryUsecase;
    private final AddSubCategoryUsecase addSubCategoryUsecase;

    @Operation(summary = "1차 카테고리 추가")
    @PostMapping("/main-category")
    @Secured("ROLE_ADMIN")
    public void addMainCategory(@AuthenticationPrincipal SecurityUserDetails userInfo, @Valid @RequestBody AddMainCategoryRequest addMainCategoryRequest) {
        addMainCategoryUsecase.addMainCategory(userInfo.getUserId(), addMainCategoryRequest.code(), addMainCategoryRequest.name());
    }

    @Operation(summary = "2차 카테고리 추가")
    @PostMapping("/sub-category")
    @Secured("ROLE_ADMIN")
    public void addSubCategory(@AuthenticationPrincipal SecurityUserDetails userInfo, @Valid @RequestBody AddSubCategoryRequest addCategoryRequest) {
        addSubCategoryUsecase.addSubCategory(userInfo.getUserId(), addCategoryRequest.mainCategoryId(), addCategoryRequest.code(), addCategoryRequest.name());
    }

}