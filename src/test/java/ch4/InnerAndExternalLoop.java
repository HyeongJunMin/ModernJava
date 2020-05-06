package ch4;

import common.Dish;
import common.Dish.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@Slf4j
public class InnerAndExternalLoop {

  List<Dish> menu = new ArrayList();

  @BeforeEach
  void setUp() {
    menu.add(new Dish("pork", false, 800, Type.MEAT));
    menu.add(new Dish("beef", false, 700, Type.MEAT));
    menu.add(new Dish("chicken", false, 400, Type.MEAT));
    menu.add(new Dish("french fries", true, 530, Type.OTHER));
    menu.add(new Dish("rice", true, 350, Type.OTHER));
    menu.add(new Dish("season fruits", true, 120, Type.OTHER));
    menu.add(new Dish("pizza", true, 550, Type.OTHER));
    menu.add(new Dish("prawns", false, 300, Type.FISH));
    menu.add(new Dish("salmon", false, 450, Type.FISH));
  }

  @Test
  void externalLoopTest() {
    List<String> highCaloricDishes = new ArrayList<>();
    Iterator<Dish> iterator = menu.iterator();
    while (iterator.hasNext()) {
      Dish dish = iterator.next();
      if (dish.getCalories() > 300) {
        highCaloricDishes.add(dish.getName());
      }
    }

    log.debug("highCaloricDishes : {}", highCaloricDishes);
    log.debug("highCaloricDishes size : {}", highCaloricDishes.size());
  }

  @Test
  void innerLoopTest() {
    List<String> highCaloricDishes = menu.stream()
        .filter(dish -> dish.getCalories() > 300)
        .map(Dish::getName)
        .collect(Collectors.toList());

    log.debug("highCaloricDishes : {}", highCaloricDishes);
    log.debug("highCaloricDishes size : {}", highCaloricDishes.size());
  }

}
