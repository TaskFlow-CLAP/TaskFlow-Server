package clap.server.adapter.inbound.web.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FindManagersResponse {

    private Long memberId;
    private String nickname;  // 닉네임 필드 추가
    private String imageUrl;  // 이미지 URL 필드 추가
    private int remainingTasks;

}
