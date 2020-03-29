package ch1;

import common.Apple;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

        //한 번만 사용할 메서드는 저 위에처럼 할 필요 없이 람다로 작성
        apples = Apple
                .filterApples(inventory,
                        a -> a.getWeight() >= 150 || Apple.Colors.GREEN.equals(a.getColor()));
        log.debug("apples size : {}", apples.size());
        apples.forEach(apple -> log.debug("apple color : {} , weight : {}", apple.getColor(), apple.getWeight()));
    }

    @Test
    void parallelStreamTest() {
        //순차 처리 방식
        List<Apple> resultList = inventory.stream().filter(a -> a.getWeight() >= 150).collect(Collectors.toList());
        
        //병렬 처리 방식
        List<Apple> parallelResultList = inventory.parallelStream().filter(a -> a.getWeight() >= 150).collect(Collectors.toList());
    }
}