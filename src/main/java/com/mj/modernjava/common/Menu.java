package com.mj.modernjava.common;

import com.mj.modernjava.common.Dish.Type;
import java.util.ArrayList;
import java.util.List;

public class Menu {

  private static List<Dish> list = new ArrayList();

  public static List<Dish> getList() {
    if (list.size() == 0) {
      setMenu();
    }
    return list;
  }

  private static void setMenu() {
    list.add(new Dish("pork", false, 800, Type.MEAT));
    list.add(new Dish("beef", false, 700, Type.MEAT));
    list.add(new Dish("chicken", false, 400, Type.MEAT));
    list.add(new Dish("french fries", true, 530, Type.OTHER));
    list.add(new Dish("rice", true, 350, Type.OTHER));
    list.add(new Dish("season fruits", true, 120, Type.OTHER));
    list.add(new Dish("pizza", true, 550, Type.OTHER));
    list.add(new Dish("prawns", false, 300, Type.FISH));
    list.add(new Dish("salmon", false, 450, Type.FISH));
  }
}
