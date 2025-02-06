package clap.server.adapter.inbound.web.admin;

import clap.server.adapter.inbound.web.dto.admin.request.SendInvitationRequest;
import clap.server.application.port.inbound.admin.SendInvitationUsecase;
import clap.server.common.annotation.architecture.WebAdapter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@Tag(name = "05. Admin [회원 관리]")
@WebAdapter
@RequiredArgsConstructor
@RequestMapping("/api/managements")
public class SendInvitationController {
    private final SendInvitationUsecase sendInvitationUsecase;

    @Operation(summary = "회원 초대 이메일 발송 API")
    @Secured("ROLE_ADMIN")
    @PostMapping("/members/invite")
    public void sendInvitation(@RequestBody @Valid SendInvitationRequest request) {
        sendInvitationUsecase.sendInvitation(request);
    }
}
