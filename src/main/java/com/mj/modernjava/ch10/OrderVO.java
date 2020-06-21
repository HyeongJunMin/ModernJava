package com.mj.modernjava.ch10;

import com.mj.modernjava.ch10.model.Trade;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderVO {
    private String customer;
    private List<Trade> tradeList = new ArrayList();
}
