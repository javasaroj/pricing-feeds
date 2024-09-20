package com.tigeranalytics.pricefeed.app.controller;

import com.tigeranalytics.pricefeed.app.entity.PricingFeed;
import com.tigeranalytics.pricefeed.app.model.PricingFeedSearchCriteria;
import com.tigeranalytics.pricefeed.app.service.PricingFeedService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(
            summary = "Add a new Pricing Feed",
            description = "Adds a new pricing feed with the specified details such as store ID, SKU, and price.",
            tags = { "Pricing Feeds" }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pricing feed created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<String> addPricingFeed(
            @Parameter(description = "Details of the pricing feed to be added", required = true)
            @RequestBody PricingFeed pricingFeed) {
        pricingFeedService.addPricingFeed(pricingFeed);
        return new ResponseEntity<>("PricingFeed created successfully", HttpStatus.CREATED);
    }

    @Operation(
            summary = "Get all Pricing Feeds",
            description = "Retrieve all pricing feeds available in the system.",
            tags = { "Pricing Feeds" }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of pricing feeds"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<List<PricingFeed>> getAllPricingFeeds() {
        List<PricingFeed> jobs = pricingFeedService.getPricingFeeds();
        return new ResponseEntity<>(jobs, HttpStatus.OK);
    }

    @Operation(
            summary = "Get a specific Pricing Feed by ID",
            description = "Retrieve a single pricing feed by its unique ID.",
            tags = { "Pricing Feeds" }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pricing feed found"),
            @ApiResponse(responseCode = "404", description = "Pricing feed not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{feedId}")
    public ResponseEntity<PricingFeed> getPricingFeedById(
            @Parameter(description = "ID of the pricing feed", required = true)
            @PathVariable Long feedId) {
        PricingFeed pricingFeed = pricingFeedService.getPricingFeedById(feedId);
        return new ResponseEntity<>(pricingFeed, HttpStatus.OK);
    }

    @Operation(
            summary = "Update a Pricing Feed",
            description = "Update the details of an existing pricing feed by specifying the feed ID and the updated feed details.",
            tags = { "Pricing Feeds" }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pricing feed updated successfully"),
            @ApiResponse(responseCode = "404", description = "Pricing feed not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{feedId}")
    public ResponseEntity<String> updatePricingFeed(
            @Parameter(description = "ID of the pricing feed to be updated", required = true)
            @PathVariable Long feedId,
            @Parameter(description = "Updated details of the pricing feed", required = true)
            @RequestBody PricingFeed pricingFeed) {
        boolean updated = pricingFeedService.updatePricingFeed(feedId, pricingFeed);
        if(updated)
            return new ResponseEntity<>("PricingFeed updated successfully", HttpStatus.OK);
        return new ResponseEntity<>("PricingFeed not found", HttpStatus.NOT_FOUND);
    }

    @Operation(
            summary = "Delete a Pricing Feed",
            description = "Delete a pricing feed from the system using the feed ID.",
            tags = { "Pricing Feeds" }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pricing feed deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Pricing feed not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{feedId}")
    public ResponseEntity<String> removePricingFeed(
            @Parameter(description = "ID of the pricing feed to be deleted", required = true)
            @PathVariable Long feedId) {
        boolean deleted = pricingFeedService.removePricingFeed(feedId);
        if(deleted)
            return new ResponseEntity<>("PricingFeed deleted successfully", HttpStatus.OK);
        return new ResponseEntity<>("PricingFeed not found", HttpStatus.NOT_FOUND);
    }

    @Operation(
            summary = "Search Pricing Feeds",
            description = "Search for pricing feeds using various criteria such as store ID, SKU, product name, and date.",
            tags = { "Pricing Feeds" }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Search results returned successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid search criteria"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/search")
    public ResponseEntity<PagedModel<EntityModel<PricingFeed>>> search(
            @Parameter(description = "Search criteria", required = true)
            @ModelAttribute PricingFeedSearchCriteria criteria) {

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
