package com.tigeranalytics.pricefeed.app.service.impl;

import com.tigeranalytics.pricefeed.app.annotation.LogPayloads;
import com.tigeranalytics.pricefeed.app.annotation.TrackExecutionTime;
import com.tigeranalytics.pricefeed.app.entity.PricingFeed;
import com.tigeranalytics.pricefeed.app.repository.PricingFeedRepository;
import com.tigeranalytics.pricefeed.app.service.PricingFeedService;
import com.tigeranalytics.pricefeed.app.specification.PricingFeedSpecificationBuilder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PricingFeedServiceImpl implements PricingFeedService {

    private final PricingFeedRepository pricingFeedRepository;

    public PricingFeedServiceImpl(PricingFeedRepository pricingFeedRepository) {
        this.pricingFeedRepository = pricingFeedRepository;
    }


    @Override
    public void addPricingFeed(PricingFeed pricingFeed) {
        pricingFeedRepository.save(pricingFeed);
    }

    @Override
    @TrackExecutionTime
    public List<PricingFeed> getPricingFeeds() {
        return pricingFeedRepository.findAll();
    }

    @Override
    @LogPayloads
    @TrackExecutionTime
    public PricingFeed getPricingFeedById(Long feedId) {
        return pricingFeedRepository.findById(feedId).orElse(null);
    }

    @Override
    public boolean updatePricingFeed(Long feedId, PricingFeed pricingFeedUpdate) {
        Optional<PricingFeed> pricingFeedOpt = pricingFeedRepository.findById(feedId);
        if(pricingFeedOpt.isPresent()) {
            PricingFeed pricingFeed = pricingFeedOpt.get();
            pricingFeed.setStoreId(pricingFeedUpdate.getStoreId());
            pricingFeed.setSku(pricingFeedUpdate.getSku());
            pricingFeed.setProductName(pricingFeedUpdate.getProductName());
            pricingFeed.setPrice(pricingFeedUpdate.getPrice());
            pricingFeed.setDate(pricingFeedUpdate.getDate());
            pricingFeedRepository.save(pricingFeed);
            return true;
        }
        return false;
    }

    @Override
    public boolean removePricingFeed(Long feedId) {
        try {
            pricingFeedRepository.deleteById(feedId);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    @TrackExecutionTime
    public Page<PricingFeed> searchPricingFeeds(String storeId, String sku,
                                                String productName,
                                                LocalDate date, Pageable pageable) {
        PricingFeedSpecificationBuilder builder = new PricingFeedSpecificationBuilder();
        if (!StringUtils.isBlank(storeId)) builder.with("storeId", "like", storeId);
        if (!StringUtils.isBlank(sku)) builder.with("sku", "like", sku);
        if (!StringUtils.isBlank(productName)) builder.with("productName", "like", productName);
        if (date != null) builder.with("date", "=", date);
        Specification<PricingFeed> spec = builder.build();
        return pricingFeedRepository.findAll(spec, pageable);
    }
}
