package com.karmazyn.logisticsdispatchsystem.common.audit.specification;

import com.karmazyn.logisticsdispatchsystem.common.audit.dto.SecurityLogFilterDto;
import com.karmazyn.logisticsdispatchsystem.common.audit.entity.SecurityLog;
import org.springframework.data.jpa.domain.Specification;

public class SecurityLogSpecification {

    public static Specification<SecurityLog> withFilter(SecurityLogFilterDto filter) {
        return (root, query, cb) -> {

            var predicates = cb.conjunction();

            if (filter.getUserId() != null) {
                predicates = cb.and(predicates,
                        cb.equal(root.get("userId"), filter.getUserId()));
            }

            if (filter.getEmail() != null) {
                predicates = cb.and(predicates,
                        cb.equal(root.get("email"), filter.getEmail()));
            }

            if (filter.getAction() != null) {
                predicates = cb.and(predicates,
                        cb.equal(root.get("action"), filter.getAction()));
            }

            if (filter.getSuccess() != null) {
                predicates = cb.and(predicates,
                        cb.equal(root.get("success"), filter.getSuccess()));
            }

            if (filter.getFrom() != null) {
                predicates = cb.and(predicates,
                        cb.greaterThanOrEqualTo(root.get("createdAt"), filter.getFrom()));
            }

            if (filter.getTo() != null) {
                predicates = cb.and(predicates,
                        cb.lessThanOrEqualTo(root.get("createdAt"), filter.getTo()));
            }

            return predicates;
        };
    }
}