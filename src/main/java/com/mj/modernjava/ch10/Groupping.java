package com.mj.modernjava.ch10;

import com.mj.modernjava.common.Dish;
import com.mj.modernjava.common.Menu;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collector;

import static com.mj.modernjava.ch10.Groupping.GroupingBuilder.groupOn;
import static java.util.stream.Collectors.groupingBy;

public class Groupping {

    enum CaloricLevel { DIET, NORMAL, FAT }
    private static List<Dish> menu;

    public Groupping() {
        menu = Menu.getList();
    }

    public static void main(String ... args) {
        System.out.println("Dishes grouped by type and caloric level: " + groupDishedByTypeAndCaloricLevel2());
        System.out.println("Dishes grouped by type and caloric level: " + groupDishedByTypeAndCaloricLevel3());
    }

    private static CaloricLevel getCaloricLevel( Dish dish ) {
        if (dish.getCalories() <= 400) {
            return CaloricLevel.DIET;
        }
        else if (dish.getCalories() <= 700) {
            return CaloricLevel.NORMAL;
        }
        else {
            return CaloricLevel.FAT;
        }
    }

    private static Map<Dish.Type, Map<CaloricLevel, List<Dish>>> groupDishedByTypeAndCaloricLevel2() {
        return menu.stream().collect(
                twoLevelGroupingBy(Dish::getType, dish -> getCaloricLevel(dish))
        );
    }

    public static <A, B, T> Collector<T, ?, Map<A, Map<B, List<T>>>> twoLevelGroupingBy(Function<? super T, ? extends A> f1, Function<? super T, ? extends B> f2) {
        return groupingBy(f1, groupingBy(f2));
    }

    private static Map<Dish.Type, Map<CaloricLevel, List<Dish>>> groupDishedByTypeAndCaloricLevel3() {
        Collector<? super Dish, ?, Map<Dish.Type, Map<CaloricLevel, List<Dish>>>> c = groupOn((Dish dish) -> getCaloricLevel(dish)).after(Dish::getType).get();
        return menu.stream().collect(c);
    }

    public static class GroupingBuilder<T, D, K> {

        private final Collector<? super T, ?, Map<K, D>> collector;

        public GroupingBuilder(Collector<? super T, ?, Map<K, D>> collector) {
            this.collector = collector;
        }

        public Collector<? super T, ?, Map<K, D>> get() {
            return collector;
        }

        public <J> GroupingBuilder<T, Map<K, D>, J> after(Function<? super T, ? extends J> classifier) {
            return new GroupingBuilder<>(groupingBy(classifier, collector));
        }

        public static <T, D, K> GroupingBuilder<T, List<T>, K> groupOn(Function<? super T, ? extends K> classifier) {
            return new GroupingBuilder<>(groupingBy(classifier));
        }

    }

}
