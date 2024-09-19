package com.tigeranalytics.pricefeed.app.configuration;

import com.tigeranalytics.pricefeed.app.entity.PricingFeed;
import org.springframework.batch.item.ItemProcessor;

public class PricingFeedProcessor implements ItemProcessor<PricingFeed, PricingFeed> {

    @Override
    public PricingFeed process(PricingFeed item) throws Exception {
        return item;
    }
}
