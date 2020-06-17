package com.mj.modernjava.ch9;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Customer {
  private int id;
  private String name;
}
