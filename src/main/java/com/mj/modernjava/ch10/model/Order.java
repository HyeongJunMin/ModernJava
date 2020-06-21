/*
 * Copyright 2005 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mj.modernjava.ch10.model;

import com.mj.modernjava.ch10.OrderVO;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class Order {

  private String customer;
  private List<Trade> trades = new ArrayList<>();

  public void addTrade( Trade trade ) {
    trades.add( trade );
  }

  public double getValue() {
    return trades.stream().mapToDouble( Trade::getValue ).sum();
  }

  public static Order newInstance(OrderVO vo) {
    Order order = new Order();
    order.setCustomer(vo.getCustomer());
    vo.getTradeList()
            .forEach(trade -> order.getTrades().add(trade));
    return order;
  }

  @Override
  public String toString() {
    String strTrades = trades.stream().map(t -> "  " + t).collect(Collectors.joining("\n", "[\n", "\n]"));
    return String.format("Order[customer=%s, trades=%s]", customer, strTrades);
  }

}
