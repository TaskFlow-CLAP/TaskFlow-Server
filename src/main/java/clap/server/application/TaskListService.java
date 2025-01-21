package clap.server.application;

import clap.server.adapter.inbound.web.dto.task.FindTaskListRequest;
import clap.server.adapter.inbound.web.dto.task.FindTaskListResponse;

import clap.server.application.port.inbound.domain.MemberService;

import clap.server.application.port.inbound.task.TaskListUsecase;

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
public class TaskListService implements TaskListUsecase {

    private final MemberService memberService;
    private final LoadTaskPort loadTaskPort;


    @Override
    public Page<FindTaskListResponse> findRequestedTaskList(Long requesterId, Pageable pageable, FindTaskListRequest findTaskListRequest) {
        Member requester = memberService.findActiveMember(requesterId);
        return loadTaskPort.findAllByRequesterId(requester.getMemberId(), pageable, findTaskListRequest);
    }
}
