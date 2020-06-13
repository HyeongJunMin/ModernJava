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
  private final Integer calories;
  private final Type type;

  public CaloricalLevel getCaloricalLevel() {
    if (this.getCalories() <= 400) return CaloricalLevel.DIET;
    else if (this.getCalories() <= 700) return CaloricalLevel.NORMAL;
    else return CaloricalLevel.FAT;
  }

  public enum Type { MEAT, FISH, OTHER }
  public enum CaloricalLevel { DIET, NORMAL, FAT }

}
