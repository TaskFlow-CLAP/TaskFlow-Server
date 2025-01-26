package clap.server.application.service.admin;

import clap.server.adapter.outbound.persistense.entity.member.MemberEntity;
import clap.server.adapter.outbound.persistense.entity.member.constant.MemberRole;
import clap.server.adapter.outbound.persistense.entity.member.constant.MemberStatus;
import clap.server.adapter.outbound.persistense.entity.task.CategoryEntity;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
@Transactional
class AddCategoryServiceTest {

    @Container
    public static ElasticsearchContainer ES_CONTAINER = new ElasticsearchContainer("docker.elastic.co/elasticsearch/elasticsearch:7.17.5")
            .withReuse(true);

    @DynamicPropertySource
    static void elasticProperties(DynamicPropertyRegistry registry) {
        // Elasticsearch 설정
        registry.add("spring.elasticsearch.uris", ES_CONTAINER::getHttpHostAddress);
    }

    @Autowired
    private AddCategoryService addCategoryService;
    @Autowired
    private EntityManager entityManager;

    @Test
    @Transactional
    @Rollback(false)
    void addMainCategory() {
        // 관리자 추가
        MemberEntity admin = MemberEntity.builder()
                .name("Admin")
                .email("admin@example.com")
                .nickname("admin.ad")
                .isReviewer(false)
                .role(MemberRole.ROLE_ADMIN)
                .departmentRole("Admin")
                .status(MemberStatus.ACTIVE)
                .password("admin123")
                .build();
        entityManager.persist(admin);

        admin = entityManager.find(MemberEntity.class, 1);
        addCategoryService.addMainCategory(admin.getMemberId(), "VM", "가상머신");

        CategoryEntity category = entityManager.find(CategoryEntity.class, 1);
        assertThat(category.getCategoryId()).isEqualTo(1);
        assertThat(category.getName()).isEqualTo("가상머신");
        assertThat(category.getCode()).isEqualTo("VM");
        assertThat(category.getAdmin().getMemberId()).isEqualTo(1);
    }

    @Test
    void addSubCategory() {
        MemberEntity admin = entityManager.find(MemberEntity.class, 1);
        addCategoryService.addSubCategory(admin.getMemberId(), 1L, "CR", "생성");

        CategoryEntity category = entityManager.find(CategoryEntity.class, 2);
        assertThat(category.getCategoryId()).isEqualTo(2);
        assertThat(category.getMainCategory().getCategoryId()).isEqualTo(1);
        assertThat(category.getName()).isEqualTo("생성");
        assertThat(category.getCode()).isEqualTo("CR");
        assertThat(category.getAdmin().getMemberId()).isEqualTo(1);
    }
}