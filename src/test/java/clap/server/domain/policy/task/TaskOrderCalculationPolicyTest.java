package clap.server.domain.policy.task;

import clap.server.domain.model.task.Task;
import clap.server.exception.DomainException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskOrderCalculationPolicyTest {

    private static final long DEFAULT_PROCESSOR_ORDER_GAP = 64L;

    @InjectMocks
    private TaskOrderCalculationPolicy policy;

    @Mock
    private Task prevTask;

    @Mock
    private Task nextTask;

    @Test
    @DisplayName("맨 위에 새로운 Task 추가 - 이전 Task가 없는 경우")
    void calculateOrderForTop_WhenPrevTaskIsNull() {
        // Arrange (준비)
        when(nextTask.getProcessorOrder()).thenReturn(3000L);

        // Act (실행)
        long order = policy.calculateOrderForTop(null, nextTask);

        // Assert (검증)
        assertEquals(3000L - DEFAULT_PROCESSOR_ORDER_GAP, order);
    }

    @Test
    @DisplayName("맨 위에 새로운 Task 추가 - 이전 Task가 존재하는 경우")
    void calculateOrderForTop_WhenPrevTaskExists() {
        // Arrange
        when(prevTask.getProcessorOrder()).thenReturn(1000L);
        when(nextTask.getProcessorOrder()).thenReturn(3000L);

        // Act
        long order = policy.calculateOrderForTop(prevTask, nextTask);

        // Assert
        assertEquals(2000L, order);
    }

    @Test
    @DisplayName("맨 아래에 새로운 Task 추가 - 다음 Task가 없는 경우")
    void calculateOrderForBottom_WhenNextTaskIsNull() {
        // Arrange
        when(prevTask.getProcessorOrder()).thenReturn(1000L);

        // Act
        long order = policy.calculateOrderForBottom(prevTask, null);

        // Assert
        assertEquals(1000L + DEFAULT_PROCESSOR_ORDER_GAP, order);
    }

    @Test
    @DisplayName("맨 아래에 새로운 Task 추가 - 다음 Task가 존재하는 경우")
    void calculateOrderForBottom_WhenNextTaskExists() {
        // Arrange
        when(prevTask.getProcessorOrder()).thenReturn(1000L);
        when(nextTask.getProcessorOrder()).thenReturn(3000L);

        // Act
        long order = policy.calculateOrderForBottom(prevTask, nextTask);

        // Assert
        assertEquals(2000L, order);
    }

    @Test
    @DisplayName("새로운 순서를 계산 - 유효한 범위 내에서 계산")
    void calculateNewProcessorOrder_WhenValid() {
        // Arrange
        long prevOrder = 1000L;
        long nextOrder = 3000L;

        // Act
        long order = policy.calculateNewProcessorOrder(prevOrder, nextOrder);

        // Assert
        assertEquals(2000L, order);
    }

    @Test
    @DisplayName("새로운 순서를 계산 - 간격이 너무 좁을 경우 예외 발생")
    void calculateNewProcessorOrder_WhenOrderGapIsTooSmall() {
        // Arrange
        long prevOrder = 1000L;
        long nextOrder = 1001L;

        // Act & Assert
        assertThrows(DomainException.class, () -> policy.calculateNewProcessorOrder(prevOrder, nextOrder));
    }

    @Test
    @DisplayName("새로운 순서를 계산 - prevTaskOrder 또는 nextTaskOrder가 null일 경우 기본값 반환")
    void calculateNewProcessorOrder_WhenNullValues() {
        // Arrange
        Long prevOrder = null;
        Long nextOrder = 3000L;

        // Act
        long order1 = policy.calculateNewProcessorOrder(prevOrder, nextOrder);
        long order2 = policy.calculateNewProcessorOrder(1000L, null);

        // Assert
        assertEquals(DEFAULT_PROCESSOR_ORDER_GAP, order1);
        assertEquals(DEFAULT_PROCESSOR_ORDER_GAP, order2);
    }
}



