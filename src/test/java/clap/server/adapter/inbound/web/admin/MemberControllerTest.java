package clap.server.adapter.inbound.web.admin;

import clap.server.adapter.outbound.persistense.entity.member.DepartmentEntity;
import clap.server.adapter.outbound.persistense.entity.member.constant.DepartmentStatus;
import clap.server.adapter.outbound.persistense.entity.member.constant.MemberRole;
import clap.server.adapter.outbound.persistense.entity.member.MemberEntity;
import clap.server.adapter.outbound.persistense.entity.member.constant.MemberStatus;
import clap.server.adapter.outbound.persistense.entity.task.CategoryEntity;
import clap.server.adapter.outbound.persistense.entity.task.LabelEntity;
import clap.server.adapter.outbound.persistense.entity.task.TaskEntity;
import clap.server.config.elastic.ElasticsearchConfig;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import clap.server.adapter.outbound.persistense.entity.task.constant.TaskStatus;
import clap.server.adapter.outbound.persistense.entity.task.constant.LabelType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.lifecycle.Startables;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.DynamicPropertyRegistry;

@Testcontainers // 테스트 컨테이너 활성화
@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EntityManager entityManager;

    // Elasticsearch 컨테이너 설정
    @Container
    public static ElasticsearchContainer ES_CONTAINER = new ElasticsearchContainer("docker.elastic.co/elasticsearch/elasticsearch:7.17.5")
            .withReuse(true);

    // Redis 컨테이너 설정 (필요 시 사용)
    @Container
    public static GenericContainer<?> REDIS_CONTAINER = new GenericContainer<>(DockerImageName.parse("redis:6.2.6"))
            .withExposedPorts(6379)
            .withReuse(true);

    @DynamicPropertySource
    static void dynamicProperties(DynamicPropertyRegistry registry) {
        // Elasticsearch 설정
        registry.add("spring.elasticsearch.uris", ES_CONTAINER::getHttpHostAddress);
        // Redis 설정
        registry.add("spring.data.redis.host", REDIS_CONTAINER::getHost);
        registry.add("spring.data.redis.port", () -> REDIS_CONTAINER.getMappedPort(6379));
    }

    @BeforeEach
    @Transactional
    @Rollback(false)
    public void setupTestData() {
        // 부서 추가
        DepartmentEntity department = DepartmentEntity.builder()
                .code("DEPT001")
                .name("IT Department")
                .status(DepartmentStatus.ACTIVE)
                .build();
        entityManager.persist(department); // department 먼저 persist

        // 관리자 추가
        MemberEntity admin = MemberEntity.builder()
                .name("Admin User")
                .email("admin@example.com")
                .nickname("Admin1")
                .isReviewer(false)
                .role(MemberRole.ROLE_ADMIN)
                .departmentRole("Admin")
                .status(MemberStatus.ACTIVE)
                .password("admin123")
                .imageUrl("http://example.com/admin.jpg")
                .notificationEnabled(true)
                .department(department) // 부서 할당
                .build();
        entityManager.persist(admin);

        // 카테고리 추가
        CategoryEntity category = CategoryEntity.builder()
                .code("CATEGORY001")
                .name("Development")
                .descriptionExample("Development tasks category")
                .isDeleted(false)
                .admin(admin)
                .build();
        entityManager.persist(category);

        // 라벨 추가
        LabelEntity label = LabelEntity.builder()
                .labelType(LabelType.EMERGENCY)
                .admin(admin)
                .isDeleted(false)
                .build();
        entityManager.persist(label);

        // 관리자 (manager) 추가
        MemberEntity manager1 = MemberEntity.builder()  // manager1 정의
                .name("Manager1")
                .email("manager1@example.com")
                .nickname("Manager1")
                .isReviewer(true)
                .role(MemberRole.ROLE_MANAGER) // 관리자로 설정
                .departmentRole("Manager")
                .status(MemberStatus.ACTIVE)
                .password("manager123")
                .imageUrl("http://example.com/manager1.jpg")
                .notificationEnabled(true)
                .department(department) // 부서 할당
                .build();
        entityManager.persist(manager1); // manager1 저장

        // 태스크 추가
        TaskEntity task = TaskEntity.builder()
                .taskCode("TASK001")
                .title("Task Title")
                .description("Task Description")
                .category(category) // 카테고리 연결
                .requester(admin)
                .taskStatus(TaskStatus.PENDING_COMPLETED)
                .processorOrder(1)
                .reviewer(admin)
                .processor(manager1)  // 여기서 manager1을 processor로 설정
                .label(label)
                .dueDate(LocalDateTime.now().plusDays(7))
                .completedAt(null)
                .build();
        entityManager.persist(task);

        // 일반 사용자 (user) 추가
        MemberEntity user = MemberEntity.builder()
                .name("User1")
                .email("user1@example.com")
                .nickname("User1")
                .isReviewer(false)
                .role(MemberRole.ROLE_USER)
                .departmentRole("User")
                .status(MemberStatus.ACTIVE)
                .password("user123")
                .imageUrl("http://example.com/user1.jpg")
                .notificationEnabled(true)
                .department(department) // 부서 할당
                .build();
        entityManager.persist(user);
        // 두 번째 관리자 추가
        MemberEntity manager2 = MemberEntity.builder()
                .name("Manager2")
                .email("manager2@example.com")
                .nickname("Manager2")
                .isReviewer(true)
                .role(MemberRole.ROLE_MANAGER)
                .departmentRole("Manager")
                .status(MemberStatus.ACTIVE)
                .password("manager123")
                .imageUrl("http://example.com/manager2.jpg")
                .notificationEnabled(true)
                .department(department)
                .build();
        entityManager.persist(manager2); // 두 번째 관리자 저장

        // 추가로 5명의 사용자 생성
        List<MemberEntity> members = new ArrayList<>();
        for (int i = 2; i <= 5; i++) {
            members.add(MemberEntity.builder()
                    .name("User" + i)
                    .email("user" + i + "@example.com")
                    .nickname("User" + i)
                    .isReviewer(i % 2 == 0)
                    .role(MemberRole.ROLE_USER)
                    .departmentRole("DepartmentUser")
                    .status(MemberStatus.ACTIVE)
                    .password("user123")
                    .imageUrl("http://example.com/user" + i + ".jpg")
                    .notificationEnabled(i % 2 != 0)
                    .department(department) // 부서 할당
                    .build());
        }

        // 멤버들을 데이터베이스에 저장
        members.forEach(entityManager::persist);
    }


    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void testFindManagers() throws Exception {
        mockMvc.perform(get("/manager"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nickname").value("Manager1"))
                .andExpect(jsonPath("$[0].imageUrl").value("http://example.com/manager1.jpg"))
                .andExpect(jsonPath("$[0].remainingTasks").value(1))
                .andExpect(jsonPath("$[1].nickname").value("Manager2"))  // 추가된 관리자
                .andExpect(jsonPath("$[1].imageUrl").value("http://example.com/manager2.jpg"))
                .andExpect(jsonPath("$[1].remainingTasks").value(0))  // 예시로 추가된 관리자들의 데이터를 검증
                .andDo(print());
    }

}
