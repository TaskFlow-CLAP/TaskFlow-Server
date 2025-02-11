package clap.server.adapter.outbound.api.kakaoWork;

import clap.server.adapter.outbound.api.data.PushNotificationTemplate;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class KakaoWorkBlockBuilder {

    private final ObjectMapper objectMapper;

    public String makeObjectBlock(PushNotificationTemplate request, String taskDetailUrl){
        return switch (request.notificationType()) {
            case TASK_REQUESTED -> makeTaskRequestBlock(request, taskDetailUrl);
            case STATUS_SWITCHED -> switch (request.message()) { // getStatusChangeType() 메서드로 추가 분기
                case "TERMINATED" -> makeTerminatedStatusBlock(request, taskDetailUrl);
                default -> makeTaskStatusBlock(request, taskDetailUrl);
            };
            case PROCESSOR_CHANGED -> makeProcessorChangeBlock(request, taskDetailUrl);
            case PROCESSOR_ASSIGNED -> makeNewProcessorBlock(request, taskDetailUrl);
            case COMMENT -> makeCommentBlock(request, taskDetailUrl);
            default -> null;
        };
    }

    private String makeTaskRequestBlock(PushNotificationTemplate request, String taskDetailUrl) {
        Object[] blocks = new Object[]{
                Map.of(
                        "type", "header",
                        "text", "TaskFlow 작업 요청 알림",
                        "style", "blue"
                ),
                Map.of(
                        "type", "text",
                        "text", "TaskFlow 작업 요청 알림",
                        "inlines", new Object[]{
                                Map.of(
                                        "type", "styled",
                                        "text", "새로운 작업이 요청되었습니다.",
                                        "bold", true
                                )
                        }
                ),
                Map.of(
                        "type", "text",
                        "text", "TaskFlow 작업 요청 알림",
                        "inlines", new Object[]{
                                Map.of(
                                        "type", "styled",
                                        "text", " - Task 제목 : " + request.taskName(),
                                        "bold", false
                                )
                        }
                ),
                Map.of(
                        "type", "text",
                        "text", "TaskFlow 작업 요청 알림",
                        "inlines", new Object[]{
                                Map.of(
                                        "type", "styled",
                                        "text", " - 요청자 : " + request.senderName(),
                                        "bold", false
                                )
                        }
                ),
                Map.of(
                        "type", "button",
                        "text", "확인하기",
                        "style", "default",
                        "action", Map.of(
                                "type", "open_system_browser",
                                "name", "button1",
                                "value", taskDetailUrl
                        )
                )
        };

        String payload;
        try {
            payload = "{" +
                    "\"email\":\"" + request.email() + "\"," +
                    "\"text\": \"신규 작업 요청 알림\"," +
                    "\"blocks\":" + objectMapper.writeValueAsString(blocks) +
                    "}";
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return payload;
    }

    private String makeNewProcessorBlock(PushNotificationTemplate request, String taskDetailUrl) {
        Object[] blocks = new Object[]{
                Map.of(
                        "type", "header",
                        "text", "TaskFlow 알림 서비스",
                        "style", "blue"
                ),
                Map.of(
                        "type", "text",
                        "text", "TaskFlow 담당자 선정 알림",
                        "inlines", new Object[]{
                                Map.of(
                                        "type", "styled",
                                        "text", "TaskFlow 담당자 선정 알림",
                                        "bold", true
                                )
                        }
                ),
                Map.of(
                        "type", "text",
                        "text", "TaskFlow 담당자 선정 알림",
                        "inlines", new Object[]{
                                Map.of(
                                        "type", "styled",
                                        "text", " - Task 제목 : " + request.taskName(),
                                        "bold", false
                                )
                        }
                ),
                Map.of(
                        "type", "text",
                        "text", "TaskFlow 담당자 선정 알림",
                        "inlines", new Object[]{
                                Map.of(
                                        "type", "styled",
                                        "text", " - 담당자 : " + request.message(),
                                        "bold", false
                                )
                        }
                ),
                Map.of(
                        "type", "button",
                        "text", "확인하기",
                        "style", "default",
                        "action", Map.of(
                                "type", "open_system_browser",
                                "name", "button1",
                                "value", taskDetailUrl
                        )
                )
        };

        String payload;
        try {
            payload = "{" +
                        "\"email\":\"" + request.email() + "\"," +
                        "\"text\":\"작업 담당자 선정 알림\"," +
                        "\"blocks\":" + objectMapper.writeValueAsString(blocks) +
                        "}";
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return payload;
    }

    private String makeProcessorChangeBlock(PushNotificationTemplate request, String taskDetailUrl) {
        Object[] blocks = new Object[]{
                Map.of(
                        "type", "header",
                        "text", "TaskFlow 알림 서비스",
                        "style", "blue"
                ),
                Map.of(
                        "type", "text",
                        "text", "TaskFlow 담당자 변경 알림",
                        "inlines", new Object[]{
                                Map.of(
                                        "type", "styled",
                                        "text", "TaskFlow 담당자 변경 알림",
                                        "bold", true
                                )
                        }
                ),
                Map.of(
                        "type", "text",
                        "text", "TaskFlow 담당자 변경 알림",
                        "inlines", new Object[]{
                                Map.of(
                                        "type", "styled",
                                        "text", " - Task 제목 : " + request.taskName(),
                                        "bold", false
                                )
                        }
                ),
                Map.of(
                        "type", "text",
                        "text", "TaskFlow 담당자 변경 알림",
                        "inlines", new Object[]{
                                Map.of(
                                        "type", "styled",
                                        "text", " - 담당자 : " + request.message(),
                                        "bold", false
                                )
                        }
                ),
                Map.of(
                        "type", "button",
                        "text", "확인하기",
                        "style", "default",
                        "action", Map.of(
                                "type", "open_system_browser",
                                "name", "button1",
                                "value", taskDetailUrl
                        )
                )
        };

        String payload;
        try {
            payload = "{" +
                    "\"email\":\"" + request.email() + "\"," +
                    "\"text\":\"작업 담당자 변경 알림\"," +
                    "\"blocks\":" + objectMapper.writeValueAsString(blocks) +
                    "}";
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }


        return payload;
    }

    private String makeCommentBlock(PushNotificationTemplate request, String taskDetailUrl) {
        Object[] blocks = new Object[]{
                Map.of(
                        "type", "header",
                        "text", "TaskFlow 알림 서비스",
                        "style", "blue"
                ),
                Map.of(
                        "type", "text",
                        "text", "TaskFlow 댓글 알림",
                        "inlines", new Object[]{
                                Map.of(
                                        "type", "styled",
                                        "text", "TaskFlow 댓글 알림",
                                        "bold", true
                                )
                        }
                ),
                Map.of(
                        "type", "text",
                        "text", "TaskFlow 댓글 알림",
                        "inlines", new Object[]{
                                Map.of(
                                        "type", "styled",
                                        "text", " - Task Title : " + request.taskName(),
                                        "bold", false
                                )
                        }
                ),
                Map.of(
                        "type", "text",
                        "text", "TaskFlow 댓글 알림",
                        "inlines", new Object[]{
                                Map.of(
                                        "type", "styled",
                                        "text", " - 작성자 : " + request.commenterName(),
                                        "bold", false
                                )
                        }
                ),
                Map.of(
                        "type", "text",
                        "text", "TaskFlow 댓글 알림",
                        "inlines", new Object[]{
                                Map.of(
                                        "type", "styled",
                                        "text", " - 내용 : " + request.message(),
                                        "bold", false
                                )
                        }
                ),
                Map.of(
                        "type", "button",
                        "text", "확인하기",
                        "style", "default",
                        "action", Map.of(
                                "type", "open_system_browser",
                                "name", "button1",
                                "value", taskDetailUrl
                        )
                )
        };

        String payload;
        try {
            payload = "{" +
                    "\"email\":\"" + request.email() + "\"," +
                    "\"text\":\"댓글 알림\"," +
                    "\"blocks\":" + objectMapper.writeValueAsString(blocks) +
                    "}";
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return payload;
    }

    private String makeTaskStatusBlock(PushNotificationTemplate request, String taskDetailUrl) {
        Object[] blocks = new Object[]{
                Map.of(
                        "type", "header",
                        "text", "TaskFlow 알림 서비스",
                        "style", "blue"
                ),
                Map.of(
                        "type", "text",
                        "text", "TaskFlow 작업 상태 변경 알림",
                        "inlines", new Object[]{
                                Map.of(
                                        "type", "styled",
                                        "text", "TaskFlow 작업 상태 변경 알림",
                                        "bold", true
                                )
                        }
                ),
                Map.of(
                        "type", "text",
                        "text", "TaskFlow 상태 변경 알림",
                        "inlines", new Object[]{
                                Map.of(
                                        "type", "styled",
                                        "text", " - Task Title : " + request.taskName(),
                                        "bold", false
                                )
                        }
                ),
                Map.of(
                        "type", "text",
                        "text", "TaskFlow 작업 상태 변경 알림",
                        "inlines", new Object[]{
                                Map.of(
                                        "type", "styled",
                                        "text", " - 상태 : " + request.message(),
                                        "bold", false
                                )
                        }
                ),
                Map.of(
                        "type", "button",
                        "text", "확인하기",
                        "style", "default",
                        "action", Map.of(
                                "type", "open_system_browser",
                                "name", "button1",
                                "value", taskDetailUrl
                        )
                )
        };

        String payload;
        try {
            payload = "{" +
                    "\"email\":\"" + request.email() + "\"," +
                    "\"text\":\"작업 상태 변경 알림\"," +
                    "\"blocks\":" + objectMapper.writeValueAsString(blocks) +
                    "}";
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return payload;
    }

    private String makeTerminatedStatusBlock(PushNotificationTemplate request, String taskDetailUrl) {
        Object[] blocks = new Object[]{
                Map.of(
                        "type", "header",
                        "text", "TaskFlow 알림 서비스",
                        "style", "blue"
                ),
                Map.of(
                        "type", "text",
                        "text", "TaskFlow 작업 종료 알림",
                        "inlines", new Object[]{
                                Map.of(
                                        "type", "styled",
                                        "text", "TaskFlow 작업 종료 알림",
                                        "bold", true
                                )
                        }
                ),
                Map.of(
                        "type", "text",
                        "text", "TaskFlow 작업 종료 알림",
                        "inlines", new Object[]{
                                Map.of(
                                        "type", "styled",
                                        "text", " - Task Title : " + request.taskName(),
                                        "bold", false
                                )
                        }
                ),
                Map.of(
                        "type", "text",
                        "text", "TaskFlow 작업 종료 알림",
                        "inlines", new Object[]{
                                Map.of(
                                        "type", "styled",
                                        "text", " - 거절 사유 : " + request.reason(),
                                        "bold", false
                                )
                        }
                ),
                Map.of(
                        "type", "button",
                        "text", "확인하기",
                        "style", "default",
                        "action", Map.of(
                                "type", "open_system_browser",
                                "name", "button1",
                                "value", taskDetailUrl
                        )
                )
        };

        String payload;
        try {
            payload = "{" +
                    "\"email\":\"" + request.email() + "\"," +
                    "\"text\":\"작업 종료 알림\"," +
                    "\"blocks\":" + objectMapper.writeValueAsString(blocks) +
                    "}";
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return payload;
    }


}
