package com.karmazyn.logisticsdispatchsystem.order.specification;

import com.karmazyn.logisticsdispatchsystem.order.dto.OrderFilterDto;
import com.karmazyn.logisticsdispatchsystem.order.entity.Order;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderSpecification {

    public Specification<Order> withFilter(OrderFilterDto filter) {
        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            // status
            if (filter.getStatus() != null) {
                predicates.add(root.get("status").in(filter.getStatus()));
            }

            // driverId
            if (filter.getDriverId() != null) {
                predicates.add(cb.equal(root.get("driver").get("id"), filter.getDriverId()));
            }

            // from (>=)
            if (filter.getFrom() != null) {
                predicates.add(cb.greaterThanOrEqualTo(
                        root.get("createdAt"),
                        filter.getFrom()
                ));
            }

            // to (<=)
            if (filter.getTo() != null) {
                predicates.add(cb.lessThanOrEqualTo(
                        root.get("createdAt"),
                        filter.getTo()
                ));
            }

            // SEARCH (LIKE)
            String search = filter.getSearch();

            if (search != null && search.isBlank()) {

                if (search.matches("\\d+")) {
                    predicates.add(cb.equal(root.get("id"), Long.valueOf(search)));
                }

                String likePattern = "%" + filter.getSearch().toLowerCase() + "%";

                Predicate byStatus = cb.like(
                        cb.lower(root.get("status").as(String.class)),
                        likePattern
                );

                predicates.add(cb.or(byStatus));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
