package clap.server.adapter.out.persistense.entity.task;

import clap.server.adapter.out.persistense.entity.common.BaseTimeEntity;
import clap.server.adapter.out.persistense.entity.member.MemberEntity;
import clap.server.adapter.out.persistense.entity.task.constant.CategoryStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "category")
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CategoryEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long categoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id", nullable = false)
    private MemberEntity admin;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "belong_category_id")
    private CategoryEntity belongCategory;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CategoryStatus categoryStatus;
}
