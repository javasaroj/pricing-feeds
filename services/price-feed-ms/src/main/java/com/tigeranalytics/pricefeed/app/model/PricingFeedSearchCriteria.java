package com.tigeranalytics.pricefeed.app.model;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class PricingFeedSearchCriteria {

    private String storeId;
    private String sku;
    private String productName;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate date;
    private int page = 0;
    private int size = 10;
    private String sortBy = "id";
    private String sortDir = "asc";
}
