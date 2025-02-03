package clap.server.adapter.inbound.web.admin;

import clap.server.adapter.inbound.security.SecurityUserDetails;
import clap.server.adapter.inbound.web.dto.label.CreateLabelRequest;
import clap.server.adapter.inbound.web.dto.label.EditLabelRequest;
import clap.server.application.port.inbound.label.AddLabelUsecase;
import clap.server.application.port.inbound.admin.DeleteLabelUsecase;
import clap.server.application.port.inbound.label.UpdateLabelUsecase;
import clap.server.common.annotation.architecture.WebAdapter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "05. Admin")
@WebAdapter
@RequiredArgsConstructor
@RequestMapping("/api/managements/labels")
public class ManagementLabelController {

    private final AddLabelUsecase addLabelUsecase;
    private final UpdateLabelUsecase updateLabelUsecase;
    private final DeleteLabelUsecase deleteLabelUsecase;

    @Operation(summary = "구분(label) 추가 API")
    @PostMapping
    @Secured({"ROLE_ADMIN"})
    public void addLabel(@AuthenticationPrincipal SecurityUserDetails userInfo,
                         @Valid @RequestBody CreateLabelRequest request) {
        addLabelUsecase.addLabel(userInfo.getUserId(), request);
    }

    @Operation(summary = "구분(label) 수정 API")
    @Parameter(name = "labelId", description = "구분(label) 고유 ID", required = true, in = ParameterIn.PATH)
    @PatchMapping("/{labelId}")
    @Secured({"ROLE_ADMIN"})
    public void updateLabel(@AuthenticationPrincipal SecurityUserDetails userInfo,
                            @PathVariable Long labelId,
                            @Valid @RequestBody EditLabelRequest request) {
        updateLabelUsecase.editLabel(userInfo.getUserId(), labelId, request);

    }

    @Operation(summary = "구분(label) 삭제 API")
    @Parameter(name = "labelId", description = "구분(label) 고유 ID", required = true, in = ParameterIn.PATH)
    @DeleteMapping("/{labelId}")
    @Secured({"ROLE_ADMIN"})
    public void deleteLabel(@AuthenticationPrincipal SecurityUserDetails userInfo,
                            @PathVariable Long labelId) {
        deleteLabelUsecase.deleteLabel(userInfo.getUserId(), labelId);
    }
}
