package clap.server.adapter.inbound.web.admin;

import clap.server.adapter.inbound.security.service.SecurityUserDetails;
import clap.server.adapter.inbound.web.dto.admin.request.RegisterMemberRequest;
import clap.server.application.port.inbound.admin.RegisterMemberUsecase;
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
@RequestMapping("/api/managements")
@RequiredArgsConstructor
public class RegisterMemberController {

    private final RegisterMemberUsecase registerMemberUsecase;

    @Operation(summary = "단일 회원 등록 API")
    @PostMapping("/members")
    @Secured("ROLE_ADMIN")
    public void registerMember(@AuthenticationPrincipal SecurityUserDetails userInfo,
                               @RequestBody @Valid RegisterMemberRequest request) {
        registerMemberUsecase.registerMember(userInfo.getUserId(), request);
    }
}