package clap.server.adapter.outbound.persistense.specification;

import clap.server.adapter.inbound.web.dto.admin.FindMemberRequest;
import clap.server.adapter.outbound.persistense.entity.member.MemberEntity;
import clap.server.adapter.outbound.persistense.entity.member.constant.MemberStatus;
import org.springframework.data.jpa.domain.Specification;

public class MemberSpecification {

    // DELETED 상태 제외
    public static Specification<MemberEntity> isNotDeleted() {
        return (root, query, cb) -> cb.notEqual(root.get("status"), MemberStatus.DELETED);
    }

    public static Specification<MemberEntity> withFilter(FindMemberRequest filterRequest) {
        return (root, query, cb) -> {
            var predicates = cb.conjunction();

            // DELETED 상태가 아닌 회원만 기본으로 조회
            predicates = cb.and(predicates, isNotDeleted().toPredicate(root, query, cb));


            if (filterRequest.getName() != null) {
                predicates = cb.and(predicates, cb.like(root.get("name"), "%" + filterRequest.getName() + "%"));
            }
            if (filterRequest.getEmail() != null) {
                predicates = cb.and(predicates, cb.like(root.get("email"), "%" + filterRequest.getEmail() + "%"));
            }
            if (filterRequest.getNickname() != null) {
                predicates = cb.and(predicates, cb.like(root.get("nickname"), "%" + filterRequest.getNickname() + "%"));
            }
            if (filterRequest.getDepartmentId() != null) {
                predicates = cb.and(predicates, cb.equal(root.get("departmentId"), filterRequest.getDepartmentId()));
            }
            if (filterRequest.getRole() != null) {
                predicates = cb.and(predicates, cb.equal(root.get("role"), filterRequest.getRole()));
            }

            return predicates;
        };
    }
}
