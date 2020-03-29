package ch2;

import common.Apple;

public class AppleGreenColorPredicate implements ApplePredicate {
    @Override
    public boolean test(Apple apple) {
        return Apple.Colors.GREEN.equals(apple.getColor());
    }
}
