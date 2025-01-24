package clap.server.application.service;

import clap.server.adapter.inbound.web.dto.admin.FindManagersResponse;
import clap.server.domain.model.member.Member;
import clap.server.application.port.inbound.domain.MemberService;
import clap.server.application.mapper.ManagersMapper;
import clap.server.application.port.inbound.domain.FindManagersUsecase;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service  // 여기 추가
@RequiredArgsConstructor
public class FindActiveManagersService implements FindManagersUsecase {

    private final MemberService memberService;
    private final ManagersMapper findManagersResponseMapper;

    @Transactional
    @Override
    public List<FindManagersResponse> execute() {

        List<Member> managers = memberService.findActiveManagers();

        // managers를 FindManagersResponse로 매핑
        return managers.stream()
                .map(findManagersResponseMapper::mapToFindManagersResponse)
                .collect(Collectors.toList());
    }
}
