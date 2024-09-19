package com.tigeranalytics.pricefeed.app.controller;

import com.tigeranalytics.pricefeed.app.entity.PricingFeed;
import com.tigeranalytics.pricefeed.app.model.PricingFeedSearchCriteria;
import com.tigeranalytics.pricefeed.app.service.PricingFeedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pricing-feeds")
public class PricingFeedController {

    private final PricingFeedService pricingFeedService;
    private final PagedResourcesAssembler<PricingFeed> pagedResourcesAssembler;

    @Autowired
    public PricingFeedController(PricingFeedService pricingFeedService,
                                 PagedResourcesAssembler<PricingFeed> pagedResourcesAssembler) {
        this.pricingFeedService = pricingFeedService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @PostMapping
    public ResponseEntity<String> addPricingFeed(@RequestBody PricingFeed pricingFeed) {
        pricingFeedService.addPricingFeed(pricingFeed);
        return new ResponseEntity<>("PricingFeed created successfully", HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<PricingFeed>> getAllPricingFeeds() {
        List<PricingFeed> jobs = pricingFeedService.getPricingFeeds();
        return new ResponseEntity<>(jobs, HttpStatus.OK);
    }

    @GetMapping("/{feedId}")
    public ResponseEntity<PricingFeed> getPricingFeedById(@PathVariable Long feedId) {
        PricingFeed pricingFeed = pricingFeedService.getPricingFeedById(feedId);
        return new ResponseEntity<>(pricingFeed, HttpStatus.OK);
    }

    @PutMapping("/{feedId}")
    public ResponseEntity<String> updatePricingFeed(@PathVariable Long feedId, @RequestBody PricingFeed pricingFeed) {
        boolean updated = pricingFeedService.updatePricingFeed(feedId, pricingFeed);
        if(updated)
            return new ResponseEntity<>("PricingFeed updated successfully", HttpStatus.OK);
        return new ResponseEntity<>("PricingFeed not found", HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{feedId}")
    public ResponseEntity<String> removePricingFeed(@PathVariable Long feedId) {
        boolean deleted = pricingFeedService.removePricingFeed(feedId);
        if(deleted)
            return new ResponseEntity<>("PricingFeed deleted successfully", HttpStatus.OK);
        return new ResponseEntity<>("PricingFeed not found", HttpStatus.NOT_FOUND);
    }

    @PostMapping("/search")
    public ResponseEntity<PagedModel<EntityModel<PricingFeed>>> search(@ModelAttribute PricingFeedSearchCriteria criteria) {

        Sort sort = criteria.getSortDir().equalsIgnoreCase("asc")
                ? Sort.by(criteria.getSortBy()).ascending()
                : Sort.by(criteria.getSortBy()).descending();

        Pageable pageable = PageRequest.of(criteria.getPage(), criteria.getSize(), sort);

        Page<PricingFeed> pricingFeeds = pricingFeedService.searchPricingFeeds(
                                                criteria.getStoreId(),
                                                criteria.getSku(),
                                                criteria.getProductName(),
                                                criteria.getDate(),
                                                pageable
                                            );
        PagedModel<EntityModel<PricingFeed>> pagedModel = pagedResourcesAssembler.toModel(pricingFeeds);

        return new ResponseEntity<>(pagedModel, HttpStatus.OK);
    }

}
