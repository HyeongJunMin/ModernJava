package ch2;

import common.Apple;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

@Slf4j
class Ch2BehaviorParamizeTest {

    List<Apple> inventory = new ArrayList();

    @BeforeEach
    void setUp() {
        inventory.add(new Apple(Apple.Colors.RED, 150));
        inventory.add(new Apple(Apple.Colors.GREEN, 100));
        inventory.add(new Apple(Apple.Colors.BLACK, 50));
    }

    @Test
    void predicateTest() {
        //작성한 필터메소드에 조건에 따라 predicate를 다르게 사용하면
        //파라미터에 따라 다른 동작을 수행한다
        Apple.filterApplesCustomPredicate(inventory, new AppleGreenColorPredicate());
        Apple.filterApplesCustomPredicate(inventory, new AppleHeavyWeightPredicate());

        //predicate 굳이 안하고 람다로 하는게 좋을듯
        Apple.filterApples(inventory, apple -> Apple.Colors.RED.equals(apple.getColor()));

        inventory.stream().filter(apple -> Apple.Colors.GREEN.equals(apple.getColor()));
    }

    @Test
    void compareTest() {
        inventory.forEach(apple -> log.debug("apple color : {}, weight : {}", apple.getColor(), apple.getWeight()));
        inventory.sort( (Apple a, Apple b) -> a.getWeight().compareTo(b.getWeight()) );
        inventory.forEach(apple -> log.debug("apple color : {}, weight : {}", apple.getColor(), apple.getWeight()));
    }
}