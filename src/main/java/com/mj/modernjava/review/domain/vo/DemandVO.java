package com.mj.modernjava.review.domain.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DemandVO {
    private Long minSpeed;
    private Long minPrice;
    private Long maxPrice;
    private int recommendLimit;
}
