package clap.server.adapter.inbound.web.member;

import clap.server.application.mapper.RetrieveAllMemberMapper;
import clap.server.application.port.inbound.management.FindAllMemberUsecase;
import clap.server.domain.model.member.Member;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {
    private final FindAllMemberUsecase findAllMemberUsecase;
    private final RetrieveAllMemberMapper retrieveAllMemberMapper;

    @Tag(name = "Admin")
    @Secured({"ROLE_ADMIN"})
    @Operation(
            summary = "전체 회원 조회 API",
            description = "모든 회원 정보를 페이징 처리하여 반환합니다.",
            parameters = {
                    @Parameter(name = "page", description = "조회할 페이지 번호 (1부터 시작, 기본값: 1)", example = "1"),
                    @Parameter(name = "size", description = "페이지 당 회원 수 (기본값: 20)", example = "20")
            }
    )
    @GetMapping
    public ResponseEntity<Page<RetrieveAllMemberResponse>> getAllMembers(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Member> members = findAllMemberUsecase.findAllMembers(pageable);
        Page<RetrieveAllMemberResponse> response = members.map(retrieveAllMemberMapper::toResponse);
        return ResponseEntity.ok(response);
    }
}
