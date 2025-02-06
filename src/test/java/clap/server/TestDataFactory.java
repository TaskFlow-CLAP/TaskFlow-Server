package clap.server;

import clap.server.adapter.inbound.web.dto.task.request.FilterTaskListRequest;
import clap.server.adapter.outbound.persistense.entity.member.constant.DepartmentStatus;
import clap.server.adapter.outbound.persistense.entity.member.constant.MemberRole;
import clap.server.adapter.outbound.persistense.entity.member.constant.MemberStatus;
import clap.server.adapter.outbound.persistense.entity.task.constant.LabelColor;
import clap.server.adapter.outbound.persistense.entity.task.constant.TaskStatus;
import clap.server.domain.model.member.Department;
import clap.server.domain.model.member.Member;
import clap.server.domain.model.member.MemberInfo;
import clap.server.domain.model.task.Category;
import clap.server.domain.model.task.Label;
import clap.server.domain.model.task.Task;
import org.springframework.data.domain.PageImpl;

import java.time.LocalDateTime;
import java.util.List;

public class TestDataFactory {

    public static Member createAdmin() {
        return Member.builder()
                .memberId(1L)
                .memberInfo(createAdminInfo())
                .admin(null)
                .kakaoworkNotificationEnabled(true)
                .agitNotificationEnabled(true)
                .emailNotificationEnabled(true)
                .imageUrl(null)
                .status(MemberStatus.ACTIVE)
                .password("1111")
                .department(createDepartment())
                .build();
    }

    public static Member createManagerWithReviewer() {
        return Member.builder()
                .memberId(2L)
                .memberInfo(createManagerWithReviewerInfo())
                .admin(createAdmin())
                .kakaoworkNotificationEnabled(true)
                .agitNotificationEnabled(true)
                .emailNotificationEnabled(true)
                .imageUrl(null)
                .status(MemberStatus.ACTIVE)
                .password("1111")
                .department(createDepartment())
                .build();
    }

    public static Member createManager() {
        return Member.builder()
                .memberId(3L)
                .memberInfo(createManagerInfo())
                .admin(createAdmin())
                .kakaoworkNotificationEnabled(true)
                .agitNotificationEnabled(true)
                .emailNotificationEnabled(true)
                .imageUrl(null)
                .status(MemberStatus.ACTIVE)
                .password("1111")
                .department(createDepartment())
                .build();
    }

    public static Member createUser() {
        return Member.builder()
                .memberId(4L)
                .memberInfo(createUserInfo())
                .admin(createAdmin())
                .kakaoworkNotificationEnabled(true)
                .agitNotificationEnabled(true)
                .emailNotificationEnabled(true)
                .imageUrl(null)
                .status(MemberStatus.ACTIVE)
                .password("1111")
                .department(createDepartment())
                .build();
    }

    public static MemberInfo createAdminInfo() {
        return new MemberInfo("홍길동(관리자)", "atom8426@naver.com", "atom.admin", false, null, MemberRole.ROLE_ADMIN, "인프라");
    }

    public static MemberInfo createManagerWithReviewerInfo() {
        return new MemberInfo("홍길동(리뷰어)", "atom8426@naver.com", "atom.manager", true, null, MemberRole.ROLE_MANAGER, "인프라");
    }

    public static MemberInfo createManagerInfo() {
        return new MemberInfo("홍길동(매니저)", "atom8426@naver.com", "atom.manager", false, null, MemberRole.ROLE_MANAGER, "인프라");
    }

    public static MemberInfo createUserInfo() {
        return new MemberInfo("홍길동(사용자)", "atom8426@naver.com", "atom.user", false, null, MemberRole.ROLE_USER, "인프라");
    }

    public static Department createDepartment() {
        return Department.builder()
                .departmentId(1L)
                .adminId(1L)
                .name("IT 부서")
                .status(DepartmentStatus.ACTIVE)
                .build();
    }

    public static Category createMainCategory() {
        return Category.builder()
                .categoryId(1L)
                .name("1차 카테고리")
                .code("VM")
                .isDeleted(false)
                .descriptionExample("메인 카테고리 입니다.")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public static Category createCategory(Category mainCategory) {
        return Category.builder()
                .categoryId(2L)
                .name("2차 카테고리")
                .code("CR")
                .isDeleted(false)
                .descriptionExample("서브 카테고리 입니다.")
                .mainCategory(mainCategory)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public static Task createTask(Long id, String taskCode, String title, TaskStatus taskStatus, Category category, LocalDateTime finishedAt, Member processor) {
        return Task.builder()
                .taskId(id)
                .taskCode(taskCode)
                .title(title)
                .description(null)
                .category(category)
                .taskStatus(taskStatus)
                .finishedAt(finishedAt)
                .requester(createUser())
                .processor(processor)
                .label(createLabel())
                .build();
    }

    public static Label createLabel() {
        return Label.builder()
                .labelId(1L)
                .admin(null)
                .labelName("레이블")
                .labelColor(LabelColor.BLUE)
                .isDeleted(false)
                .build();
    }

    public static PageImpl<Task> createTaskPage(List<Task> tasks) {
        return new PageImpl<>(tasks);
    }
}
