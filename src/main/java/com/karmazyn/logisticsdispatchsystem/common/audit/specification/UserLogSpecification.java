package com.karmazyn.logisticsdispatchsystem.common.audit.specification;

import com.karmazyn.logisticsdispatchsystem.common.audit.dto.UserLogFilterDto;
import com.karmazyn.logisticsdispatchsystem.common.audit.entity.UserLog;
import org.springframework.data.jpa.domain.Specification;

public class UserLogSpecification {

    public static Specification<UserLog> withFilter(UserLogFilterDto filter) {
        return (root, query, cb) -> {

            var predicates = cb.conjunction(); // TRUE

            // userId
            if (filter.getUserId() != null) {
                predicates = cb.and(predicates,
                        cb.equal(root.get("userId"), filter.getUserId()));
            }

            // email
            if (filter.getEmail() != null) {
                predicates = cb.and(predicates,
                        cb.equal(root.get("email"), filter.getEmail()));
            }

            // action
            if (filter.getAction() != null) {
                predicates = cb.and(predicates,
                        cb.equal(root.get("action"), filter.getAction()));
            }

            // entity
            if (filter.getEntity() != null) {
                predicates = cb.and(predicates,
                        cb.equal(root.get("entity"), filter.getEntity()));
            }

            // from (>=)
            if (filter.getFrom() != null) {
                predicates = cb.and(predicates,
                        cb.greaterThanOrEqualTo(root.get("createdAt"), filter.getFrom()));
            }

            // to (<=)
            if (filter.getTo() != null) {
                predicates = cb.and(predicates,
                        cb.lessThanOrEqualTo(root.get("createdAt"), filter.getTo()));
            }

            return predicates;
        };
    }
}