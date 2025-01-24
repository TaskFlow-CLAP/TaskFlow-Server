package clap.server.application.service;

import clap.server.adapter.inbound.web.dto.admin.FindManagersResponse;
import clap.server.domain.model.member.Member;
import clap.server.application.port.inbound.domain.MemberService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FindActiveManagersService {

    private final MemberService memberService;

    @Transactional
    public List<FindManagersResponse> execute() {

        List<Member> managers = memberService.findActiveManagers();

        // 빈 리스트라도 매핑하여 반환
        return managers.stream().map(manager -> {
            int remainingTasks = memberService.getRemainingTasks(manager.getMemberId());
            String nickname = memberService.getMemberNickname(manager.getMemberId());
            String imageUrl = memberService.getMemberImageUrl(manager.getMemberId());

            return new FindManagersResponse(
                    manager.getMemberId(),
                    nickname,
                    imageUrl,
                    remainingTasks
            );
        }).collect(Collectors.toList());
    }
}
