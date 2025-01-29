package clap.server.adapter.inbound.web.admin;

import clap.server.adapter.inbound.security.SecurityUserDetails;
import clap.server.adapter.inbound.web.dto.label.AddAndEditLabelRequest;
import clap.server.application.port.inbound.admin.AddLabelUsecase;
import clap.server.application.port.inbound.admin.UpdateLabelUsecase;
import clap.server.common.annotation.architecture.WebAdapter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "05. Admin")
@WebAdapter
@RequiredArgsConstructor
@RequestMapping("/api/management/label")
public class UpdateLabelController {

    private final AddLabelUsecase addLabelUsecase;
    private final UpdateLabelUsecase updateLabelUsecase;

    @Operation(summary = "구분(label) 추가 API")
    @PostMapping
    @Secured({"ROLE_ADMIN"})
    public void addLabel(@AuthenticationPrincipal SecurityUserDetails userInfo,
                         @RequestBody AddAndEditLabelRequest request) {
        addLabelUsecase.addLabel(userInfo.getUserId(), request);
    }

    @Operation(summary = "구분(label) 수정 API")
    @PatchMapping("/{labelId}")
    @Secured({"ROLE_ADMIN"})
    public void updateLabel(@AuthenticationPrincipal SecurityUserDetails userInfo,
                            @PathVariable Long labelId,
                            @RequestBody AddAndEditLabelRequest request) {
        updateLabelUsecase.editLabel(userInfo.getUserId(), labelId, request);

    }
}
