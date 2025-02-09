package clap.server.adapter.inbound.web.notification;


import clap.server.adapter.inbound.security.service.SecurityUserDetails;
import clap.server.application.port.inbound.notification.SubscribeSseUsecase;
import clap.server.common.annotation.architecture.WebAdapter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Deprecated
@Tag(name = "SSE 관리 - 회원 등록(최초 접속시)")
@WebAdapter
@RequestMapping("/api/sse")
@RequiredArgsConstructor
public class SubscribeEmitterController {

    private final SubscribeSseUsecase subscribeSseUsecase;

    @Operation(summary = "회원이 최초 접속 시 SSE(실시간 알림)에 연결하는 API")
    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(@AuthenticationPrincipal SecurityUserDetails userInfo) {
        return subscribeSseUsecase.subscribe(userInfo.getUserId());
    }
}
