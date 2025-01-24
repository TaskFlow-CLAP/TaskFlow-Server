package clap.server.adapter.inbound.web.admin;

import clap.server.adapter.inbound.security.SecurityUserDetails;
import clap.server.application.port.inbound.management.RegisterMemberUsecase;
import clap.server.common.annotation.architecture.WebAdapter;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Qualifier;

@WebAdapter
@RequestMapping("/api/admin")
public class RegisterMemberCsvController {
    private final RegisterMemberUsecase registerMemberUsecase;

    public RegisterMemberCsvController(@Qualifier("registerMemberCsvService") RegisterMemberUsecase registerMemberUsecase) {
        this.registerMemberUsecase = registerMemberUsecase;
    }

    @Operation(summary = "CSV 파일로 회원 등록 API")
    @PostMapping("/members/upload")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<String> registerMembersFromCsv(
            @AuthenticationPrincipal SecurityUserDetails userInfo,
            @RequestParam("file") MultipartFile file) {
        try {
            int addedCount = registerMemberUsecase.registerMembersFromCsv(userInfo.getUserId(), file);
            return ResponseEntity.ok(addedCount + "명의 회원이 등록되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("CSV 형식 오류: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("회원 등록 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
}