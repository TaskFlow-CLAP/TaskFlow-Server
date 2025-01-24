package clap.server.adapter.inbound.web.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class FindManagersResponse {

    private Long memberId;
    private String nickname;
    private String imageUrl;
    private int remainingTasks;

    public static List<FindManagersResponse> emptyListResponse() {
        return List.of();
    }

}


