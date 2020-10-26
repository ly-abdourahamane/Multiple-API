package com.mandat.amoulanfe.victim;

import com.mandat.amoulanfe.utils.BasicSpecificationFactory;
import com.mandat.amoulanfe.utils.Text;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class VictimSpecifivationFactory extends BasicSpecificationFactory {

    public static Specification<Victim> getVictimsByFilter(VictimFilter filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (Text.isValid(filter.getSearch())) {
                Predicate searchPredicate = cb.or(
                        normalizedLike(cb, root.get(Victim_.FIRSTNAME), filter.getSearch()),
                        normalizedLike(cb, root.get(Victim_.LASTNAME), filter.getSearch()),
                        normalizedLike(cb, root.get(Victim_.DESCRIPTION), filter.getSearch()),
                        normalizedLike(cb, root.get(Victim_.DEATH_DATE), filter.getSearch()),
                        normalizedLike(cb, root.get(Victim_.AGE), filter.getSearch()),
                        normalizedLike(cb, root.get(Victim_.CITY), filter.getSearch()));
                predicates.add(searchPredicate);
            }

            if (filter.getFirstname() != null) {
                Predicate firstnamePredicate = normalizedLike(cb, root.get(Victim_.FIRSTNAME), filter.getFirstname());
                predicates.add(firstnamePredicate);
            }

            if (filter.getLastname() != null) {
                Predicate lastnamePredicate = normalizedLike(cb, root.get(Victim_.LASTNAME), filter.getLastname());
                predicates.add(lastnamePredicate);
            }

            if (filter.getCity() != null) {
                Predicate cityPredicate = normalizedLike(cb, root.get(Victim_.CITY), filter.getCity());
                predicates.add(cityPredicate);
            }

            query.distinct(true);
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
