package clap.server.adapter.inbound.web.member;

import clap.server.application.mapper.RetrieveAllMemberMapper;
import clap.server.application.port.inbound.management.FindAllMemberUsecase;
import clap.server.domain.model.member.Member;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {
    private final FindAllMemberUsecase findAllMemberUsecase;
    private final RetrieveAllMemberMapper retrieveAllMemberMapper;

    @Operation(summary = "전체 회원 조회 API", description = "모든 회원 정보를 반환합니다.")
    @GetMapping
    public ResponseEntity<List<RetrieveAllMemberResponse>> getAllMembers() {
        List<Member> members = findAllMemberUsecase.findAllMembers();
        List<RetrieveAllMemberResponse> response = retrieveAllMemberMapper.toResponseList(members);
        return ResponseEntity.ok(response);
    }
}
