package clap.server.adapter.outbound.persistense.specification;

import clap.server.adapter.inbound.web.dto.admin.FindMemberRequest;
import clap.server.adapter.outbound.persistense.entity.member.MemberEntity;
import org.springframework.data.jpa.domain.Specification;

public class MemberSpecification {

    public static Specification<MemberEntity> withFilter(FindMemberRequest filterRequest) {
        return (root, query, cb) -> {
            var predicates = cb.conjunction();

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
