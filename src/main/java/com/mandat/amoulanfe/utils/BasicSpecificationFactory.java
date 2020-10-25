package com.mandat.amoulanfe.utils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public abstract class BasicSpecificationFactory {

    protected static Predicate normalizedLike(CriteriaBuilder cb, Path<String> fieldPath, String value) {
        String normalizedSearch = Text.normalize(value);
        return cb.like(cb.function("", String.class, cb.lower(fieldPath)), "%" + normalizedSearch + "%");
    }

    protected static Predicate normalizedEqual(CriteriaBuilder cb, Path<String> fieldPath, String value) {
        String normalizedSearch = Text.normalize(value);
        return cb.equal(cb.function("unaccent", String.class, cb.lower(fieldPath)), normalizedSearch);
    }

    protected static List<Predicate> normalizedEqual(CriteriaBuilder cb, Path<String> fieldPath, List<String> values) {
        List<Predicate> predicatesList = new ArrayList<>();
        values.forEach(item -> predicatesList.add(normalizedEqual(cb, fieldPath, item)));

        return predicatesList;
    }

    protected static Predicate createPredicateFromList(CriteriaBuilder cb, List<Predicate> predicateList) {
        Predicate result = null;
        for (Predicate predicate : predicateList) {
            if (result == null) {
                result = cb.or(predicate);
            } else {
                result = cb.or(result, predicate);
            }
        }

        return result;
    }

}

