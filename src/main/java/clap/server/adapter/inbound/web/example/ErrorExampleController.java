package clap.server.adapter.inbound.web.example;

import clap.server.common.annotation.architecture.WebAdapter;
import clap.server.common.annotation.swagger.ApiErrorCodes;
import clap.server.common.annotation.swagger.DevelopOnlyApi;
import clap.server.exception.code.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "*. 에러 응답")
@WebAdapter
@RequestMapping("/api/examples")
public class ErrorExampleController {

    @GetMapping("/global")
    @DevelopOnlyApi
    @Operation(summary = "글로벌 (aop, 서버 내부 오류등)  관련 에러 코드 나열")
    @ApiErrorCodes(GlobalErrorCode.class)
    public void getGlobalErrorCode() {}

    @GetMapping("/member")
    @DevelopOnlyApi
    @Operation(summary = "회원 도메인 관련 에러 코드 나열")
    @ApiErrorCodes(MemberErrorCode.class)
    public void getMemberErrorCode() {}

    @GetMapping("/auth")
    @DevelopOnlyApi
    @Operation(summary = "인증 및 인가 관련 에러 코드 나열")
    @ApiErrorCodes(AuthErrorCode.class)
    public void getAuthErrorCode() {}

    @GetMapping("/task")
    @DevelopOnlyApi
    @Operation(summary = "작업 도메인 관련 에러 코드 나열")
    @ApiErrorCodes(TaskErrorCode.class)
    public void getTaskErrorCode() {}

    @GetMapping("/notification")
    @DevelopOnlyApi
    @Operation(summary = "알림 도메인 및 웹훅 관련 에러 코드 나열")
    @ApiErrorCodes(NotificationErrorCode.class)
    public void getNotificationErrorCode() {}

    @GetMapping("/comment")
    @DevelopOnlyApi
    @Operation(summary = "댓글 도메인 관련 에러 코드 나열")
    @ApiErrorCodes(CommentErrorCode.class)
    public void getCommentErrorCode() {}

    @GetMapping("/statistic")
    @DevelopOnlyApi
    @Operation(summary = "작업 통계 관련 에러 코드 나열")
    @ApiErrorCodes(LabelErrorCode.class)
    public void getStatisticsErrorCode() {}

    @GetMapping("/label")
    @DevelopOnlyApi
    @Operation(summary = "라벨 도메인 관련 에러 코드 나열")
    @ApiErrorCodes(LabelErrorCode.class)
    public void getLabelErrorCode() {}

    @GetMapping("/department")
    @DevelopOnlyApi
    @Operation(summary = "부서 도메인 관련 에러 코드 나열")
    @ApiErrorCodes(DepartmentErrorCode.class)
    public void getDepartmentErrorCode() {}

    @GetMapping("/file")
    @DevelopOnlyApi
    @Operation(summary = "파일 처리 관련 에러 코드 나열")
    @ApiErrorCodes(FileErrorcode.class)
    public void getFileErrorCode() {}
}
