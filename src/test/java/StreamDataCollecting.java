import static java.util.stream.Collectors.averagingInt;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.maxBy;
import static java.util.stream.Collectors.reducing;
import static java.util.stream.Collectors.summarizingInt;
import static java.util.stream.Collectors.summingInt;

import common.Dish;
import common.Dish.Type;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

@Slf4j
public class StreamDataCollecting {

  List<Dish> menu = new ArrayList();

  @BeforeAll
  public void setUp() {
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
  public void reducingAndSumUp() {
    //요소 수 계산
    long count = menu.stream().count();
    //최대값
    Comparator<Dish> dishComparator = Comparator.comparingInt(Dish::getCalories);
    Optional<Dish> collect = menu.stream().collect(maxBy(dishComparator));
    //요약 연산
    Integer totalCalories = menu.stream().collect(summingInt(Dish::getCalories));
    Double averageCalories = menu.stream().collect(averagingInt(Dish::getCalories));
    IntSummaryStatistics summary = menu.stream().collect(summarizingInt(Dish::getCalories));
    log.info("summary : {}", summary);

    //문자열 연결 : 내부적으로 StringBuilder를 이용해서 각 객체의 toString()을 호출하여 문자열 연결
    String shortMenu = menu.stream().map(Dish::getName).collect(Collectors.joining(", "));

    //범용 리듀싱 요약 연산(Collectors.reducing())
    menu.stream().collect(reducing(0, Dish::getCalories, (i, j) -> i + j));
  }
}
