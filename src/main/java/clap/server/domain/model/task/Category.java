package clap.server.domain.model.task;

import clap.server.domain.model.common.BaseTime;
import clap.server.domain.model.member.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category extends BaseTime {
    private Long categoryId;
    private Member admin;
    private Category mainCategory;
    private String code;
    private String name;
    private boolean isDeleted;
    private String descriptionExample;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static Category createMainCategory(Member admin, String code, String name) {
        return Category.builder()
                .admin(admin)
                .code(code)
                .name(name)
                .build();
    }

    public static Category createSubCategory(Member admin, Category mainCategory, String code, String name) {
        return Category.builder()
                .mainCategory(mainCategory)
                .admin(admin)
                .code(code)
                .name(name)
                .build();
    }

    public void updateMainCategory(Category mainCategory){
        this.mainCategory = mainCategory;
    }
}

