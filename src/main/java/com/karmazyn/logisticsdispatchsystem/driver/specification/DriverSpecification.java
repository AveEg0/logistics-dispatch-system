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

            //SEARCH
            String search = filter.getSearch();

            if (search != null && !search.isBlank()) {

                List<Predicate> searchPredicates = new ArrayList<>();

                String likePattern = "%" + search.toLowerCase() + "%";

                Predicate byName = cb.like(
                        cb.lower(root.get("name")),
                        likePattern);
                searchPredicates.add(byName);

                Predicate byLocation = cb.like(
                                cb.lower(root.get("currentLocation")),
                                likePattern);
                searchPredicates.add(byLocation);

                Predicate byEmail = cb.like(
                                cb.lower(root.get("user").get("email")),
                                likePattern);
                searchPredicates.add(byEmail);

                if (search.matches("\\d+")) {
                    Predicate byId = cb.equal(root.get("id"), Long.valueOf(search));
                    searchPredicates.add(byId);
                }
                
                predicates.add(cb.or(searchPredicates.toArray(new Predicate[0])));

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

