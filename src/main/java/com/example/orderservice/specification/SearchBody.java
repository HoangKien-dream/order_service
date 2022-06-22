package com.example.orderservice.specification;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SearchBody {
//    private String keyword;
    private int page;
    private int limit;
    private String keyword;
    private int maxPrice;
    private int minPrice;
    private String endDate;
    private String startDate;
    private int status;
}
