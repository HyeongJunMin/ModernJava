package common;

import ch2.ApplePredicate;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Apple implements Item{

    @Override
    public boolean test() {
        return true;
    }


    public enum Colors {
        GREEN, RED, BLACK
    }

    private Colors color;
    private Integer weight;

    public static List<Apple> filterApples(List<Apple> inventory, Predicate<Apple> p){
        List<Apple> resultList = inventory.stream().filter(apple -> p.test(apple)).collect(Collectors.toList());

        return resultList;
    }

    public static List<Apple> filterApplesCustomPredicate(List<Apple> inventory, ApplePredicate p) {
        List<Apple> result = new ArrayList();
        for (Apple apple : inventory) {
            if (p.test(apple)) {
               result.add(apple);
            }
        }
        return result;
    }

    public static boolean isGreenApple(Apple apple) {
        return Colors.GREEN.equals(apple.getColor());
    }

    public static boolean isHeavyApple(Apple apple) {
       return apple.getWeight() >= 150;
    }
}
