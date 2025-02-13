package clap.server.application.service.task;

import clap.server.adapter.inbound.web.dto.task.response.FindManagersResponse;
import clap.server.adapter.outbound.persistense.entity.task.constant.TaskStatus;
import clap.server.application.port.inbound.domain.MemberService;
import clap.server.application.port.inbound.task.FindManagersUsecase;
import clap.server.application.port.outbound.task.LoadTaskPort;
import clap.server.common.annotation.architecture.ApplicationService;
import clap.server.domain.model.member.Member;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static clap.server.application.mapper.response.TaskResponseMapper.toFindManagersResponse;

@ApplicationService
@RequiredArgsConstructor
public class FindManagersService implements FindManagersUsecase {

    private final MemberService memberService;
    private final LoadTaskPort loadTaskPort;

    @Transactional
    @Override
    public List<FindManagersResponse> findManagers() {
//        List<TaskStatus> targetStatuses = List.of(TaskStatus.IN_PROGRESS, TaskStatus.IN_REVIEWING);
        List<Member> managers = memberService.findActiveManagers();
        return managers.stream()
                .map(manager -> {
                    int remainingTasks = manager.getInProgressTaskCount() + manager.getInReviewingTaskCount();
                    return toFindManagersResponse(manager, remainingTasks);
                }).toList();
    }
}
;