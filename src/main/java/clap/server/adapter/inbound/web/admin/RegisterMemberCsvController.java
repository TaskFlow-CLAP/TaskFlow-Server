package clap.server.adapter.inbound.web.admin;

import clap.server.adapter.inbound.security.service.SecurityUserDetails;
import clap.server.application.port.inbound.admin.RegisterMemberCSVUsecase;
import clap.server.common.annotation.architecture.WebAdapter;
import clap.server.common.utils.FileTypeValidator;
import clap.server.exception.AdapterException;
import clap.server.exception.ApplicationException;
import clap.server.exception.code.FileErrorcode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Tag(name = "05. Admin [회원 관리]")
@WebAdapter
@RequestMapping("/api/managements")
public class RegisterMemberCsvController {
    private final RegisterMemberCSVUsecase registerMemberCSVUsecase;

    public RegisterMemberCsvController(RegisterMemberCSVUsecase registerMemberCSVUsecase) {
        this.registerMemberCSVUsecase = registerMemberCSVUsecase;
    }

    @Operation(summary = "CSV 파일로 회원 등록 API")
    @PostMapping("/members/upload")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<String> registerMembersFromCsv(
            @AuthenticationPrincipal SecurityUserDetails userInfo,
            @RequestParam("file") MultipartFile file) throws IOException {
        if (!FileTypeValidator.validCSVFile(file.getInputStream())) {
            throw new AdapterException(FileErrorcode.UNSUPPORTED_FILE_TYPE);
        }
        int addedCount = registerMemberCSVUsecase.registerMembersFromCsv(userInfo.getUserId(), file);
        return ResponseEntity.ok(addedCount + "명의 회원이 등록되었습니다.");
    }
}