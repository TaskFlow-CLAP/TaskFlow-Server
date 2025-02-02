package clap.server.adapter.inbound.web.comment;

import clap.server.adapter.inbound.security.SecurityUserDetails;
import clap.server.adapter.inbound.web.dto.task.PostAndEditCommentRequest;
import clap.server.application.port.inbound.comment.PostCommentUsecase;
import clap.server.common.annotation.architecture.WebAdapter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "02. Task [생성/수정]", description = "작업 생성/수정 API")
@WebAdapter
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment")
public class PostCommentController {

    private final PostCommentUsecase postCommentUsecase;

    @Operation(summary = "댓글 작성")
    @Parameter(name = "taskId", description = "댓글 작성할 작업 고유 ID", required = true, in = ParameterIn.PATH)
    @PostMapping("/{taskId}")
    @Secured({"ROLE_MANAGER", "ROLE_USER"})
    public void createComment(
            @AuthenticationPrincipal SecurityUserDetails userInfo,
            @PathVariable Long taskId,
            @RequestBody(required = true) PostAndEditCommentRequest request){
        postCommentUsecase.save(userInfo.getUserId(), taskId, request);
    }

    @Operation(summary = "댓글 작성(첨부 파일)")
    @Parameter(name = "taskId", description = "댓글 작성할 작업 고유 ID", required = true, in = ParameterIn.PATH)
    @PostMapping("/attachment/{taskId}")
    @Secured({"ROLE_MANAGER", "ROLE_USER"})
    public void createAttachmentComment(
            @AuthenticationPrincipal SecurityUserDetails userInfo,
            @PathVariable Long taskId,
            @RequestPart(name = "attachment") @NotNull List<MultipartFile> attachments) {
        postCommentUsecase.saveCommentAttachment(userInfo.getUserId(), taskId, attachments);
    }

}
