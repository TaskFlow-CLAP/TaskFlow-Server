package clap.server.application.service.task;

import clap.server.adapter.inbound.web.dto.common.PageResponse;
import clap.server.adapter.inbound.web.dto.task.*;

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
    public PageResponse<FilterRequestedTasksResponse> findTasksRequestedByUser(Long requesterId, Pageable pageable, FilterTaskListRequest findTaskListRequest) {
        Member requester = memberService.findActiveMember(requesterId);
        Page<FilterRequestedTasksResponse> filterRequestedTasksResponses = loadTaskPort.findTasksRequestedByUser(requester.getMemberId(), pageable, findTaskListRequest);
        return PageResponse.from(filterRequestedTasksResponses);
    }

    @Override
    public PageResponse<FilterAssignedTaskListResponse> findTasksAssignedByManager(Long processorId, Pageable pageable, FilterTaskListRequest findTaskListRequest) {
        Member processor = memberService.findActiveMember(processorId);
        Page<FilterAssignedTaskListResponse> filterAssignedTaskListResponses = loadTaskPort.findTasksAssignedByManager(processor.getMemberId(), pageable, findTaskListRequest);
        return PageResponse.from(filterAssignedTaskListResponses);
    }

    @Override
    public PageResponse<FilterPendingApprovalResponse> findPendingApprovalTasks(Long managerId, Pageable pageable, FilterTaskListRequest filterTaskListRequest) {
        memberService.findActiveMember(managerId);
        Page<FilterPendingApprovalResponse> filterPendingApprovalResponses = loadTaskPort.findPendingApprovalTasks(pageable, filterTaskListRequest);
        return PageResponse.from(filterPendingApprovalResponses);
    }

    @Override
    public PageResponse<FilterAllTasksResponse> findAllTasks(Long managerId, Pageable pageable, FilterTaskListRequest filterTaskListRequest) {
        memberService.findActiveMember(managerId);
        Page<FilterAllTasksResponse> filterAllTasksResponses = loadTaskPort.findAllTasks(pageable, filterTaskListRequest);
        return PageResponse.from(filterAllTasksResponses);
    }
}
