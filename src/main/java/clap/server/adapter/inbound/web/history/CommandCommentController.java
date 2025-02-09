package clap.server.adapter.inbound.web.history;

import clap.server.adapter.inbound.security.service.SecurityUserDetails;
import clap.server.adapter.inbound.web.dto.history.request.EditCommentRequest;
import clap.server.application.port.inbound.history.DeleteCommentUsecase;
import clap.server.application.port.inbound.history.EditCommentUsecase;
import clap.server.common.annotation.architecture.WebAdapter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "03. Task History", description = "히스토리 및 댓글 관련 API")
@WebAdapter
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommandCommentController {

    private final EditCommentUsecase editCommentUsecase;
    private final DeleteCommentUsecase deleteCommentUsecase;

    @Deprecated
    @Operation(summary = "댓글 수정")
    @Parameter(name = "commentId", description = "수정할 댓글 고유 ID", required = true, in = ParameterIn.PATH)
    @PatchMapping("/{commentId}")
    @Secured({"ROLE_MANAGER", "ROLE_USER"})
    public void editComment(
            @AuthenticationPrincipal SecurityUserDetails userInfo,
            @PathVariable Long commentId,
            @Valid @RequestBody EditCommentRequest request) {
        editCommentUsecase.editComment(userInfo.getUserId(), commentId, request);
    }

    @Operation(summary = "댓글 삭제", description = "첨부파일 댓글일 경우 request body에 삭제할 파일 ID를 리스트로 전달")
    @Parameter(name = "commentId", description = "수정할 댓글 고유 ID", required = true, in = ParameterIn.PATH)
    @DeleteMapping("/{commentId}")
    @Secured({"ROLE_MANAGER", "ROLE_USER"})
    public void deleteComment(
            @AuthenticationPrincipal SecurityUserDetails userInfo,
            @PathVariable Long commentId) {
        deleteCommentUsecase.deleteComment(userInfo.getUserId(), commentId);
    }

}
