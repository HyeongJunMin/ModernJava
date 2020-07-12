package com.mj.modernjava.review.domain.vo;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DemandVO {
    private Long minSpeed;
    private Long minPrice;
    private Long maxPrice;
    private int recommendLimit;
}
