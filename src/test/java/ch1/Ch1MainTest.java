package ch1;

import common.Apple;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

@Slf4j
class Ch1MainTest {

    List<Apple> inventory = new ArrayList();

    @BeforeEach
    void setup() {
        inventory.add(new Apple(Apple.Colors.RED, 150));
        inventory.add(new Apple(Apple.Colors.GREEN, 100));
        inventory.add(new Apple(Apple.Colors.BLACK, 50));
    }

    @Test
    void logTest() {
      log.debug("log test confirm");
    }

    @Test
    void streamTest() {
        //코드 넘겨주기 예제
        //사과를 녹색사과만 골라내달라고 했다가 150그람 이상 사과만 골라내달라고하면?
        List<Apple> apples = Apple.filterApples(inventory, Apple::isGreenApple);
        log.debug("apples size : {}", apples.size());
        apples.forEach(apple -> log.debug("apple color : {} , weight : {}", apple.getColor(), apple.getWeight()));

        apples = Apple.filterApples(inventory, Apple::isHeavyApple);
        log.debug("apples size : {}", apples.size());
        apples.forEach(apple -> log.debug("apple color : {} , weight : {}", apple.getColor(), apple.getWeight()));
    }
}