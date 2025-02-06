package clap.server.adapter.inbound.web.admin;

import clap.server.adapter.inbound.web.dto.admin.request.DeleteMemberRequest;
import clap.server.application.port.inbound.admin.DeleteMemberUsecase;
import clap.server.common.annotation.architecture.WebAdapter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "05. Admin [회원 관리]")
@WebAdapter
@RequiredArgsConstructor
@RequestMapping("/api/managements")
public class DeleteMemberController {
    private final DeleteMemberUsecase deleteMemberUsecase;

    @Transactional
    @Operation(summary = "회원 삭제 API")
    @Secured("ROLE_ADMIN")
    @DeleteMapping("/members")
    public void deleteMember(@RequestBody @Valid DeleteMemberRequest deleteMemberRequest) {
        deleteMemberUsecase.deleteMember(deleteMemberRequest.memberId());
    }
}
