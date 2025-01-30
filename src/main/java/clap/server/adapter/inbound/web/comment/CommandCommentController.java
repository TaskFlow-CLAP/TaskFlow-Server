package clap.server.adapter.inbound.web.comment;

import clap.server.adapter.inbound.security.SecurityUserDetails;
import clap.server.adapter.inbound.web.dto.task.PostAndEditCommentRequest;
import clap.server.application.port.inbound.comment.CommandCommentUsecase;
import clap.server.common.annotation.architecture.WebAdapter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
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
public class CommandCommentController {

    private final CommandCommentUsecase commandCommentUsecase;

    @Operation(summary = "댓글 수정")
    @Parameter(name = "commentId", description = "댓글 작성할 작업 고유 ID", required = true, in = ParameterIn.PATH)
    @PatchMapping("/{commentId}")
    @Secured({"ROLE_MANAGER", "ROLE_USER"})
    public void editComment(
            @AuthenticationPrincipal SecurityUserDetails userInfo,
            @PathVariable Long commentId,
            @RequestBody PostAndEditCommentRequest request) {
        commandCommentUsecase.updateComment(userInfo.getUserId(), commentId, request);
    }
}
