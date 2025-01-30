package clap.server.adapter.inbound.web.comment;

import clap.server.adapter.inbound.security.SecurityUserDetails;
import clap.server.adapter.inbound.web.dto.task.PostAndEditCommentRequest;
import clap.server.application.port.inbound.comment.PostCommentUsecase;
import clap.server.common.annotation.architecture.WebAdapter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "02. Task", description = "작업 생성/수정 API")
@WebAdapter
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment")
public class PostCommentController {

    private final PostCommentUsecase postCommentUsecase;

    @Operation(summary = "댓글 작성")
    @PostMapping("/{taskId}")
    @Secured({"ROLE_MANAGER", "ROLE_USER"})
    public void createTask(
            @AuthenticationPrincipal SecurityUserDetails userInfo,
            @PathVariable Long taskId,
            @RequestBody(required = true) PostAndEditCommentRequest request){
        postCommentUsecase.save(userInfo.getUserId(), taskId, request);
    }
}
