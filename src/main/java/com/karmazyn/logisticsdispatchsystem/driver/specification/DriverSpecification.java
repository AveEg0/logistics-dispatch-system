package com.karmazyn.logisticsdispatchsystem.driver.specification;

import com.karmazyn.logisticsdispatchsystem.driver.dto.DriverFilterDto;
import com.karmazyn.logisticsdispatchsystem.driver.entity.Driver;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DriverSpecification {

    public Specification<Driver> withFilter(DriverFilterDto filter) {
        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            //NAME
            if (filter.getName() != null) {
                predicates.add(
                        cb.like(
                                cb.lower(root.get("name")),
                                "%" + filter.getName().toLowerCase() + "%"
                        )
                );
            }

            // STATUS
            if (filter.getStatus() != null) {
                predicates.add(
                        cb.equal(root.get("status"), filter.getStatus())
                );
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}

