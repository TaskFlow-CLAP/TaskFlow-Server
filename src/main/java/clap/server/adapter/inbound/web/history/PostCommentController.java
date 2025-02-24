package clap.server.adapter.inbound.web.history;

import clap.server.adapter.inbound.security.service.SecurityUserDetails;
import clap.server.adapter.inbound.web.dto.history.request.CreateCommentRequest;
import clap.server.adapter.outbound.persistense.entity.log.constant.LogStatus;
import clap.server.application.port.inbound.history.SaveCommentAttachmentUsecase;
import clap.server.application.port.inbound.history.SaveCommentUsecase;
import clap.server.common.annotation.architecture.WebAdapter;
import clap.server.common.annotation.log.LogType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "03. Task History")
@WebAdapter
@RequiredArgsConstructor
@RequestMapping("/api/tasks")
public class PostCommentController {

    private final SaveCommentUsecase saveCommentUsecase;
    private final SaveCommentAttachmentUsecase saveCommentAttachmentUsecase;
    @LogType(LogStatus.COMMENT_ADDED)
    @Operation(summary = "댓글 작성")
    @Parameter(name = "taskId", description = "댓글 작성할 작업 고유 ID", required = true, in = ParameterIn.PATH)
    @PostMapping("/{taskId}/comments")
    @Secured({"ROLE_MANAGER", "ROLE_USER"})
    public void createComment(
            @AuthenticationPrincipal SecurityUserDetails userInfo,
            @PathVariable Long taskId,
            @Valid @RequestBody CreateCommentRequest request){
        saveCommentUsecase.save(userInfo.getUserId(), taskId, request);
    }

    @Operation(summary = "댓글 작성(첨부 파일)")
    @Parameter(name = "taskId", description = "댓글 작성할 작업 고유 ID", required = true, in = ParameterIn.PATH)
    @PostMapping(value = "/{taskId}/comments/attachment", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @Secured({"ROLE_MANAGER", "ROLE_USER"})
    public void createAttachmentComment(
            @AuthenticationPrincipal SecurityUserDetails userInfo,
            @PathVariable Long taskId,
            @Parameter(content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE))
            @RequestPart(name = "attachment") @NotNull MultipartFile attachment) {
        saveCommentAttachmentUsecase.saveCommentAttachment(userInfo.getUserId(), taskId, attachment);
    }

}
