package clap.server.notification;

import clap.server.adapter.inbound.web.dto.notification.FindNotificationListResponse;
import clap.server.adapter.outbound.persistense.entity.notification.constant.NotificationType;
import clap.server.application.port.outbound.notification.LoadNotificationPort;
import clap.server.application.service.notification.FindNotificationListService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class NotificationServiceTest {

    @Mock
    private LoadNotificationPort loadNotificationPort;

    @InjectMocks
    private FindNotificationListService findNotificationListService;


    @Test
    public void testFindNotificationList() {
        //Given
        // 목록 조회 테스트이므로 여러개의 데이터를 List에 저장
        FindNotificationListResponse findNotificationListResponse = new FindNotificationListResponse(
                1L, 1L, NotificationType.PROCESSOR_ASSIGNED, 1L, "VM 생성해주세요", "이규동", LocalDateTime.now()
        );
        FindNotificationListResponse findNotificationListResponse2 = new FindNotificationListResponse(
                1L, 1L, NotificationType.PROCESSOR_CHANGED, 1L, "VM 생성해주세요", "이규동", LocalDateTime.now()
        );
        FindNotificationListResponse findNotificationListResponse3 = new FindNotificationListResponse(
                1L, 1L, NotificationType.STATUS_SWITCHED, 1L, "VM 생성해주세요", "진행중", LocalDateTime.now()
        );

        List<FindNotificationListResponse> notificationList = List.of(
                findNotificationListResponse, findNotificationListResponse2, findNotificationListResponse3
        );

        Page<FindNotificationListResponse> page = new PageImpl<>(notificationList);
        Pageable pageable = Pageable.ofSize(3);

        //Mock
        when(loadNotificationPort.findAllByReceiverId(1L, pageable)).thenReturn(page);

        //When
        Page<FindNotificationListResponse> result = findNotificationListService.findNotificationList(1L, pageable);

        //Then
        assertEquals(3, result.getContent().size());

        assertEquals("VM 생성해주세요", result.getContent().get(0).taskTitle());
        assertEquals("VM 생성해주세요", result.getContent().get(1).taskTitle());
        assertEquals("VM 생성해주세요", result.getContent().get(2).taskTitle());
    }
}
