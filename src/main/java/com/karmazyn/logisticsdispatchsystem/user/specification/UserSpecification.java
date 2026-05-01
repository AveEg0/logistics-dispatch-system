package com.karmazyn.logisticsdispatchsystem.user.specification;

import com.karmazyn.logisticsdispatchsystem.user.dto.UserFilterDto;
import com.karmazyn.logisticsdispatchsystem.user.entity.User;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserSpecification {

    public Specification<User> withFilter(UserFilterDto filter) {
        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (filter.getEmail() != null && !filter.getEmail().isBlank()) {
                predicates.add(
                        cb.like(
                                cb.lower(root.get("email")),
                                "%" + filter.getEmail().toLowerCase() + "%"
                        )
                );
            }

            if (filter.getRole() != null) {
                predicates.add(
                        cb.equal(root.get("role"), filter.getRole())
                );
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
