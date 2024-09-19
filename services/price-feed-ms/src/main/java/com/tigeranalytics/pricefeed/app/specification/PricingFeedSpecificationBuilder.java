package com.tigeranalytics.pricefeed.app.specification;

import com.tigeranalytics.pricefeed.app.entity.PricingFeed;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PricingFeedSpecificationBuilder {

    private final List<SearchCriteria> params;

    public PricingFeedSpecificationBuilder() {
        this.params = new ArrayList<>();
    }

    public PricingFeedSpecificationBuilder with(String key, String operation, Object value) {
        params.add(new SearchCriteria(key, value, operation));
        return this;
    }

    public Specification<PricingFeed> build() {
        if (params.isEmpty()) return null;
        List<Specification<PricingFeed>> specs = params.stream()
                .map(PricingFeedSpecification::new)
                .collect(Collectors.toList());
        Specification<PricingFeed> result = specs.get(0);

        for (int i = 1; i < specs.size(); i++) {
            result = Specification.where(result).and(specs.get(i));
        }
        return result;
    }
}
