package clap.server.adapter.outbound.persistense.mapper;

import clap.server.adapter.outbound.persistense.entity.member.DepartmentEntity;
import clap.server.adapter.outbound.persistense.entity.member.MemberEntity;
import clap.server.domain.model.member.Department;
import clap.server.domain.model.member.Member;
import clap.server.domain.model.member.MemberInfo;
import org.hibernate.Hibernate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class MemberPersistenceMapper {

    @Autowired
    protected DepartmentPersistenceMapper departmentPersistenceMapper;

    @Mapping(target = "memberInfo", expression = "java(memberEntityToMemberInfo(entity))")
    @Mapping(target = "department", expression = "java(mapDepartment(entity))")
    @Mapping(target = "admin", ignore = true)
    @Mapping(source = "entity.createdAt", target = "createdAt")
    @Mapping(source = "entity.updatedAt", target = "updatedAt")
    @Mapping(source = "entity.memberId", target = "memberId")
    public abstract Member toDomain(MemberEntity entity);

    protected MemberInfo memberEntityToMemberInfo(MemberEntity memberEntity) {
        if (memberEntity == null) {
            return null;
        }

        DepartmentEntity departmentEntity = memberEntity.getDepartment();
        Department department = (departmentEntity != null && Hibernate.isInitialized(departmentEntity))
                ? departmentPersistenceMapper.toDomain(departmentEntity)
                : null;

        return MemberInfo.builder()
                .name(memberEntity.getName())
                .email(memberEntity.getEmail())
                .nickname(memberEntity.getNickname())
                .role(memberEntity.getRole())
                .departmentRole(memberEntity.getDepartmentRole())
                .isReviewer(memberEntity.isReviewer())
                .department(department)
                .build();
    }

    protected Department mapDepartment(MemberEntity entity) {
        DepartmentEntity department = entity.getDepartment();
        if (department == null) {
            return null;
        }

        if (!Hibernate.isInitialized(department)) {
            return null;
        }

        return departmentPersistenceMapper.toDomain(department);
    }

    @Mapping(source = "memberInfo.name", target = "name")
    @Mapping(source = "memberInfo.email", target = "email")
    @Mapping(source = "memberInfo.nickname", target = "nickname")
    @Mapping(source = "memberInfo.role", target = "role")
    @Mapping(source = "memberInfo.departmentRole", target = "departmentRole")
    @Mapping(source = "memberInfo.department", target = "department")
    @Mapping(source = "memberInfo.reviewer", target = "isReviewer")
    @Mapping(source = "admin", target = "admin", qualifiedByName = "toEntity")
    @Mapping(source = "createdAt", target = "createdAt")
    @Mapping(source = "updatedAt", target = "updatedAt")
    @Mapping(source = "memberId", target = "memberId")
    public abstract MemberEntity toEntity(Member member);


    @Named("toDomain")
    protected Member toDomainAdmin(MemberEntity admin) {
        if (admin == null) return null;
        return Member.builder()
                .memberId(admin.getMemberId())
                .memberInfo(MemberInfo.builder()
                        .name(admin.getName())
                        .email(admin.getEmail())
                        .nickname(admin.getNickname())
                        .isReviewer(admin.isReviewer())
                        .build())
                .createdAt(admin.getCreatedAt())
                .updatedAt(admin.getUpdatedAt())
                .build();
    }

    @Named("toEntity")
    protected MemberEntity toEntityAdmin(Member admin) {
        if (admin == null) return null;
        return MemberEntity.builder()
                .memberId(admin.getMemberId())
                .name(admin.getMemberInfo().getName())
                .email(admin.getMemberInfo().getEmail())
                .nickname(admin.getMemberInfo().getNickname())
                .isReviewer(admin.getMemberInfo().isReviewer())
                .department(departmentPersistenceMapper.toEntity(admin.getMemberInfo().getDepartment()))
                .role(admin.getMemberInfo().getRole())
                .departmentRole(admin.getMemberInfo().getDepartmentRole())
                .createdAt(admin.getCreatedAt())
                .updatedAt(admin.getUpdatedAt())
                .build();
    }
}
