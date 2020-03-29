package common;

import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Apple {

    public enum Colors {
        GREEN, RED, BLACK
    }

    private Colors color;
    private Integer weight;

    public static List<Apple> filterApples(List<Apple> inventory, Predicate<Apple> p){
        List<Apple> resultList = inventory.stream().filter(apple -> p.test(apple)).collect(Collectors.toList());

        return resultList;
    }

    public static boolean isGreenApple(Apple apple) {
        return Colors.GREEN.equals(apple.getColor());
    }

    public static boolean isHeavyApple(Apple apple) {
       return apple.getWeight() >= 150;
    }
}
