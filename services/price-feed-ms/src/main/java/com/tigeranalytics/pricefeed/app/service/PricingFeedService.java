package com.tigeranalytics.pricefeed.app.service;

import com.tigeranalytics.pricefeed.app.entity.PricingFeed;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface PricingFeedService {
    
    void addPricingFeed(PricingFeed pricingFeed);

    List<PricingFeed> getPricingFeeds();

    PricingFeed getPricingFeedById(Long feedId);

    boolean updatePricingFeed(Long feedId, PricingFeed pricingFeed);

    boolean removePricingFeed(Long feedId);

    Page<PricingFeed> searchPricingFeeds(String storeId, String sku, String productName, LocalDate date, Pageable pageable);
}
