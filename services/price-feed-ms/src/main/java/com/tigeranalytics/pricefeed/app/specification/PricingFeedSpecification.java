package com.tigeranalytics.pricefeed.app.specification;

import com.tigeranalytics.pricefeed.app.entity.PricingFeed;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

public class PricingFeedSpecification implements Specification<PricingFeed> {

    private final SearchCriteria criteria;

    public PricingFeedSpecification(SearchCriteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<PricingFeed> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        if (criteria.getOperation().equalsIgnoreCase(">")) {
            return builder.greaterThanOrEqualTo(root.get(criteria.getKey()), criteria.getValue().toString());
        } else if (criteria.getOperation().equalsIgnoreCase("<")) {
            return builder.lessThanOrEqualTo(root.get(criteria.getKey()), criteria.getValue().toString());
        } else if (criteria.getOperation().equalsIgnoreCase("=")) {
            if (criteria.getKey().equalsIgnoreCase("storeId") || criteria.getKey().equalsIgnoreCase("sku") || criteria.getKey().equalsIgnoreCase("productName")) {
                return builder.equal(builder.lower(root.get(criteria.getKey())), criteria.getValue().toString().toLowerCase());
            } else {
                return builder.equal(root.get(criteria.getKey()), criteria.getValue());
            }
        } else if (criteria.getOperation().equalsIgnoreCase("like")) {
            return builder.like(builder.lower(root.get(criteria.getKey())), "%" + criteria.getValue().toString().toLowerCase() + "%");
        }
        return null;
    }
}
