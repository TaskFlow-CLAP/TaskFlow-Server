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

    public static Category createSubCategory(Member admin, Category mainCategory, String code, String name, String descriptionExample) {
        return Category.builder()
                .mainCategory(mainCategory)
                .admin(admin)
                .code(code)
                .name(name)
                .descriptionExample(descriptionExample != null ? descriptionExample : "")
                .build();
    }

    public void updateCategory(Member admin, String name, String code, String descriptionExample){
        this.admin = admin;
        this.name = name;
        this.code = code;
        this.descriptionExample = descriptionExample != null ? descriptionExample : "";
    }

    public void deleteCategory(Member admin) {
        this.admin = admin;
        this.isDeleted = true;
    }
}

