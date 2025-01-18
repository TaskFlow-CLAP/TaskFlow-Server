package clap.server.adapter.inbound.web.admin;

import clap.server.adapter.inbound.web.dto.admin.RegisterMemberRequest;
import clap.server.application.RegisterMemberService;
import clap.server.common.annotation.architecture.WebAdapter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@WebAdapter
@RequiredArgsConstructor
@RequestMapping("/api/managements")
public class RegisterMemberController {
    private final RegisterMemberService memberService;

    @PostMapping("/members")
    public void registerMember(@RequestBody @Valid RegisterMemberRequest request) {
        memberService.registerMember(1l, request);
    }
}