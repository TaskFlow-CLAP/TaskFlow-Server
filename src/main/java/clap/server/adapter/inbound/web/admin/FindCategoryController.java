package clap.server.adapter.inbound.web.admin;

import clap.server.adapter.inbound.web.dto.admin.response.FindAllCategoryResponse;
import clap.server.adapter.inbound.web.dto.admin.response.FindMainCategoryResponse;
import clap.server.adapter.inbound.web.dto.admin.response.FindSubCategoryResponse;
import clap.server.application.port.inbound.admin.FindAllCategoryUsecase;
import clap.server.application.port.inbound.admin.FindMainCategoryUsecase;
import clap.server.application.port.inbound.admin.FindSubCategoryUsecase;
import clap.server.common.annotation.architecture.WebAdapter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Tag(name = "02. Task [카테고리]", description = "카테고리 조회 API")
@WebAdapter
@RequiredArgsConstructor
@RequestMapping("/api")
public class FindCategoryController {
    private final FindAllCategoryUsecase findAllCategoryUsecase;
    private final FindMainCategoryUsecase findmainCategoryUsecase;
    private final FindSubCategoryUsecase findsubCategoryUsecase;

    @Operation(summary = "모든 카테고리 조회")
    @GetMapping("/category")
    @Secured({"ROLE_USER", "ROLE_MANAGER", "ROLE_ADMIN"})
    public ResponseEntity<List<FindAllCategoryResponse>> findAllCategory() {
        return ResponseEntity.ok(findAllCategoryUsecase.findAllCategory());
    }

    @Operation(summary = "1차 카테고리 목록 조회")
    @GetMapping("/main-category")
    @Secured({"ROLE_USER", "ROLE_MANAGER", "ROLE_ADMIN"})
    public ResponseEntity<List<FindMainCategoryResponse>> findMainCategory() {
        return ResponseEntity.ok(findmainCategoryUsecase.findMainCategory());
    }

    @Operation(summary = "2차 카테고리 목록 조회")
    @GetMapping("/sub-category")
    @Secured({"ROLE_USER", "ROLE_MANAGER", "ROLE_ADMIN"})
    public ResponseEntity<List<FindSubCategoryResponse>> findSubCategory() {
        return ResponseEntity.ok(findsubCategoryUsecase.findSubCategory());
    }

    @Operation(summary = "2차 카테고리 단일 조회")
    @GetMapping("/sub-categories/{categoryId}")
    @Secured({"ROLE_USER", "ROLE_MANAGER", "ROLE_ADMIN"})
    public ResponseEntity<FindSubCategoryResponse> findOneSubCategory(@PathVariable Long categoryId) {
        return ResponseEntity.ok(findsubCategoryUsecase.findOneSubCategory(categoryId));
    }
}
