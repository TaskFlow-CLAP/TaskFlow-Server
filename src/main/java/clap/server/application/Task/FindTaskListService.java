package clap.server.application.Task;

import clap.server.adapter.inbound.web.dto.task.FilterTaskListRequest;
import clap.server.adapter.inbound.web.dto.task.FilterTaskListResponse;

import clap.server.adapter.inbound.web.dto.task.FilterPendingApprovalResponse;
import clap.server.application.port.inbound.domain.MemberService;

import clap.server.application.port.inbound.task.FindTaskListUsecase;

import clap.server.application.port.outbound.task.LoadTaskPort;
import clap.server.common.annotation.architecture.ApplicationService;
import clap.server.domain.model.member.Member;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;



@ApplicationService
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FindTaskListService implements FindTaskListUsecase {

    private final MemberService memberService;
    private final LoadTaskPort loadTaskPort;


    @Override
    public Page<FilterTaskListResponse> findTasksRequestedByUser(Long requesterId, Pageable pageable, FilterTaskListRequest findTaskListRequest) {
        Member requester = memberService.findActiveMember(requesterId);
        return loadTaskPort.findTasksRequestedByUser(requester.getMemberId(), pageable, findTaskListRequest);
    }

    @Override
    public Page<FilterPendingApprovalResponse> findPendingApprovalTasks(Long managerId, Pageable pageable, FilterTaskListRequest filterTaskListRequest) {
        memberService.findActiveMember(managerId);
        return loadTaskPort.findPendingApprovalTasks(pageable, filterTaskListRequest);
    }
}
