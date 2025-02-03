package clap.server.application.mapper;

import clap.server.adapter.inbound.web.dto.admin.FindManagersResponse;
import clap.server.domain.model.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ManagersMapper {

    public static FindManagersResponse toFindManagersResponse(Member manager, int remainingTasks) {
        return new FindManagersResponse(
                manager.getMemberId(),
                manager.getNickname(),
                manager.getImageUrl(),
                remainingTasks
        );
    }
}
