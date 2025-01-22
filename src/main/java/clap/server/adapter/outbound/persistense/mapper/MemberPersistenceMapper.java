package clap.server.adapter.outbound.persistense.mapper;

import clap.server.adapter.outbound.persistense.entity.member.MemberEntity;
import clap.server.domain.model.member.Member;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {DepartmentPersistenceMapper.class})
public interface MemberPersistenceMapper {

    @Mapping(source = "name", target = "memberInfo.name")
    @Mapping(source = "email", target = "memberInfo.email")
    @Mapping(source = "nickname", target = "memberInfo.nickname")
    @Mapping(source = "role", target = "memberInfo.role")
    @Mapping(source = "departmentRole", target = "memberInfo.departmentRole")
    @Mapping(source = "department", target = "memberInfo.department") // Department 변환
    @Mapping(source = "admin", target = "admin")
    Member toDomain(final MemberEntity entity);


    @Mapping(source = "memberInfo.name", target = "name")
    @Mapping(source = "memberInfo.email", target = "email")
    @Mapping(source = "memberInfo.nickname", target = "nickname")
    @Mapping(source = "memberInfo.role", target = "role")
    @Mapping(source = "memberInfo.departmentRole", target = "departmentRole")
    @Mapping(source = "memberInfo.department", target = "department") // Department 변환
    @Mapping(source = "admin", target = "admin")
    MemberEntity toEntity(Member member);

    default boolean mapIsReviewer(Member member) {
        return member.getMemberInfo().isReviewer();
    }

    default boolean mapIsReviewer(MemberEntity entity) {
        return entity.isReviewer();
    }
}


