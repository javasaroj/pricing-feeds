package com.tigeranalytics.pricefeed.app.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PricingFeedSearchCriteria {

    private String storeId;
    private String sku;
    private String productName;
    private LocalDate date;
    private int page = 0;
    private int size = 10;
    private String sortBy = "id";
    private String sortDir = "asc";
}
