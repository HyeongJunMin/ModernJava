package com.mj.modernjava.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class Dish {
  private final String name;
  private final boolean vegeterian;
  private final int calories;
  private final Type type;

  public enum Type { MEAT, FISH, OTHER }
}
