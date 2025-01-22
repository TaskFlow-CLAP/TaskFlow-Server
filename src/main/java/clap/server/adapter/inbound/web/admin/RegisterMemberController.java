package clap.server.adapter.inbound.web.admin;

import clap.server.adapter.inbound.web.dto.admin.RegisterMemberRequest;
import clap.server.application.port.inbound.management.RegisterMemberUsecase;
import clap.server.common.annotation.architecture.WebAdapter;
import clap.server.adapter.inbound.security.SecurityUserDetails;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@WebAdapter
@RequiredArgsConstructor
@RequestMapping("/api/managements")
public class RegisterMemberController {
    private final RegisterMemberUsecase registerMemberUsecase;

    @PostMapping("/members")
    @Secured("ROLE_ADMIN")
    public void registerMember(@AuthenticationPrincipal SecurityUserDetails userInfo,
                               @RequestBody @Valid RegisterMemberRequest request){
        registerMemberUsecase.registerMember(userInfo.getUserId(), request);
    }
}