package clap.server.application.port.inbound.domain;

import clap.server.adapter.inbound.web.dto.admin.FindManagersResponse;
import clap.server.domain.model.member.Member;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FindManagersUsecaseImpl implements FindManagersUsecase {

    private final MemberService memberService;

    @Override
    @Transactional
    public List<FindManagersResponse> execute() {
        List<Member> managers = memberService.findActiveManagers();

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
