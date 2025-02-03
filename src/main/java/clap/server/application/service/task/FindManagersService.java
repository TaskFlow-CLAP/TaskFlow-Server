package clap.server.application.service.task;

import clap.server.adapter.inbound.web.dto.task.response.FindManagersResponse;
import clap.server.application.port.inbound.task.FindManagersUsecase;
import clap.server.application.port.inbound.domain.MemberService;
import clap.server.common.annotation.architecture.ApplicationService;
import clap.server.domain.model.member.Member;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static clap.server.application.mapper.TaskResponseMapper.toFindManagersResponse;

@ApplicationService
@RequiredArgsConstructor
public class FindManagersService implements FindManagersUsecase {

    private final MemberService memberService;

    @Transactional
    @Override
    public List<FindManagersResponse> findManagers() {
        List<Member> managers = memberService.findActiveManagers();
        return managers.stream()
                .map(manager -> {
                    int remainingTasks = memberService.getRemainingTasks(manager.getMemberId());
                    return toFindManagersResponse(manager, remainingTasks);
                }).toList();
    }
}
;