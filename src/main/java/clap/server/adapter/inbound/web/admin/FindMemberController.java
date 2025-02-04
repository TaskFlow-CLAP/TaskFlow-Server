package clap.server.adapter.inbound.web.admin;

import clap.server.adapter.inbound.web.dto.admin.request.FindMemberRequest;
import clap.server.adapter.inbound.web.dto.admin.response.RetrieveAllMemberResponse;
import clap.server.application.mapper.RetrieveAllMemberMapper;
import clap.server.application.port.inbound.admin.FindAllMembersUsecase;
import clap.server.application.port.inbound.admin.FindMembersWithFilterUsecase;
import clap.server.domain.model.member.Member;
import clap.server.exception.ApplicationException;
import clap.server.exception.code.MemberErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/managements")
@RequiredArgsConstructor
public class FindMemberController {
    private final FindAllMembersUsecase findAllMembersUsecase;
    private final FindMembersWithFilterUsecase findMembersWithFilterUsecase;
    private final RetrieveAllMemberMapper retrieveAllMemberMapper;

    @Tag(name = "05. Admin")
    @Secured({"ROLE_ADMIN"})
    @Operation(
            summary = "전체 회원 조회 API",
            description = "모든 회원 정보를 페이징 처리하여 반환하거나 조건에 맞는 회원 정보를 반환합니다.",
            parameters = {
                    @Parameter(name = "page", description = "조회할 페이지 번호 (0부터 시작, 기본값: 0)", example = "0"),
                    @Parameter(name = "pageSize", description = "페이지 당 회원 수 (기본값: 20)", example = "20")
            }
    )
    @GetMapping("/members")
    public ResponseEntity<Page<RetrieveAllMemberResponse>> getAllMembers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int pageSize,
            @RequestBody FindMemberRequest filterRequest) {

        if (filterRequest.name() != null && filterRequest.name().trim().isEmpty()) {
            throw ApplicationException.from(MemberErrorCode.NAME_CANNOT_BE_EMPTY);
        }

        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Member> members;

        if (filterRequest.name() != null || filterRequest.email() != null || filterRequest.nickName() != null ||
                filterRequest.departmentName() != null || filterRequest.role() != null) {
            members = findMembersWithFilterUsecase.findMembersWithFilter(pageable, filterRequest);
        } else {
            members = findAllMembersUsecase.findAllMembers(pageable);
        }

        Page<RetrieveAllMemberResponse> response = members.map(retrieveAllMemberMapper::toResponse);
        return ResponseEntity.ok(response);
    }
}
