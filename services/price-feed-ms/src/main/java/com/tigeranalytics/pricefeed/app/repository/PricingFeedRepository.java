package com.tigeranalytics.pricefeed.app.repository;

import com.tigeranalytics.pricefeed.app.entity.PricingFeed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PricingFeedRepository extends JpaRepository<PricingFeed, Long>, JpaSpecificationExecutor<PricingFeed> {


}
