package com.karmazyn.logisticsdispatchsystem.order.specification;

import com.karmazyn.logisticsdispatchsystem.order.dto.OrderFilterDto;
import com.karmazyn.logisticsdispatchsystem.order.entity.Order;
import com.karmazyn.logisticsdispatchsystem.order.entity.OrderStatus;
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

            if (search != null && !search.isBlank()) {

                List<Predicate> searchPredicates = new ArrayList<>();

                if (search.matches("\\d+")) {
                    searchPredicates.add(
                            cb.equal(root.get("id"), Long.valueOf(search))
                    );
                }

                // STATUS search
                try {
                    OrderStatus statusEnum =
                            OrderStatus.valueOf(search.toUpperCase());

                    searchPredicates.add(
                            cb.equal(root.get("status"), statusEnum)
                    );

                } catch (IllegalArgumentException ignored) {
                }

                // TEXT search
                String likePattern = "%" + search.toLowerCase() + "%";

                searchPredicates.add(
                        cb.like(
                                cb.lower(root.get("pickupLocation")),
                                likePattern
                        )
                );

                searchPredicates.add(
                        cb.like(
                                cb.lower(root.get("deliveryLocation")),
                                likePattern
                        )
                );

                searchPredicates.add(
                        cb.like(
                                cb.lower(root.get("description")),
                                likePattern
                        )
                );

                predicates.add(
                        cb.or(searchPredicates.toArray(new Predicate[0]))
                );
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
