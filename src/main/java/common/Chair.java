package common;

import lombok.*;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Chair {

    public enum Color {
        IVORY, BLACK, WHITE
    }

    private String name;
    private Color color;
    private Integer weight;

    public static List<Chair> filterChairs(List<Chair> inventory, Predicate<Chair> p){
        List<Chair> resultList = inventory.stream().filter(apple -> p.test(apple)).collect(Collectors.toList());
        return resultList;
    }
}
