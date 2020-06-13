package com.mj.modernjava.ch2;

import com.mj.modernjava.common.Apple;

public class AppleGreenColorPredicate implements ApplePredicate {
    @Override
    public boolean test(Apple apple) {
        return Apple.Colors.GREEN.equals(apple.getColor());
    }
}
