package clap.server.application.mapper;

import clap.server.adapter.inbound.web.dto.admin.FindManagersResponse;
import clap.server.domain.model.member.Member;
import clap.server.application.port.inbound.domain.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ManagersMapper {

    private final MemberService memberService;

    public FindManagersResponse mapToFindManagersResponse(Member manager) {
        int remainingTasks = memberService.getRemainingTasks(manager.getMemberId());
        String nickname = memberService.getMemberNickname(manager.getMemberId());
        String imageUrl = memberService.getMemberImageUrl(manager.getMemberId());

        return new FindManagersResponse(
                manager.getMemberId(),
                nickname,
                imageUrl,
                remainingTasks
        );
    }
}
