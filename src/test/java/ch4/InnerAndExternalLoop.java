package ch4;

import common.Dish;
import common.Dish.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

@Slf4j
public class InnerAndExternalLoop {

  List<Dish> menu = new ArrayList();

//  @BeforeAll
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
//        .map(d -> d.getName())
        .collect(Collectors.toList());

    log.debug("highCaloricDishes : {}", highCaloricDishes);
    log.debug("highCaloricDishes size : {}", highCaloricDishes.size());
  }

  @Test
  public void oijdf() {
    Date today = new Date(1588293000000L);
    System.out.println("todat : " + today);
    Date today2 = new Date(1588293000L);
    System.out.println("today2 : " + today2);
    Date now = new Date();


    IntStream.range(0, 10).forEach(i -> {
      if (i == 4)
        return;
      log.debug("i : {}",i);
    });
  }

  private boolean isExtendableDate(Date endDate) {
    return (endDate.getTime() > 253402268399000L) ? false : true;
  }
}
