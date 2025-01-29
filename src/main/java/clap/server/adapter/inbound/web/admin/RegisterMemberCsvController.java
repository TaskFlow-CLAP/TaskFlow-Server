package clap.server.adapter.inbound.web.admin;

import clap.server.adapter.inbound.security.SecurityUserDetails;
import clap.server.application.port.inbound.management.RegisterMemberUsecase;
import clap.server.common.annotation.architecture.WebAdapter;
import clap.server.exception.ApplicationException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "05. Admin")
@WebAdapter
@RequestMapping("/api/managements")
public class RegisterMemberCsvController {
    private final RegisterMemberUsecase registerMemberUsecase;

    public RegisterMemberCsvController(RegisterMemberUsecase registerMemberUsecase) {
        this.registerMemberUsecase = registerMemberUsecase;
    }

    @Operation(summary = "CSV 파일로 회원 등록 API")
    @PostMapping("/members/upload")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<String> registerMembersFromCsv(
            @AuthenticationPrincipal SecurityUserDetails userInfo,
            @RequestParam("file") MultipartFile file) {
        int addedCount = registerMemberUsecase.registerMembersFromCsv(userInfo.getUserId(), file);
        return ResponseEntity.ok(addedCount + "명의 회원이 등록되었습니다.");
    }
}
