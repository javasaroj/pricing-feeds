package com.tigeranalytics.pricefeed.app.specification;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class SearchCriteria implements Serializable {
    private String key;
    private Object value;
    /** E.g., "=", ">", "<", "like" **/
    private String operation;
}