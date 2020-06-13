package com.mj.modernjava.ch2;

import com.mj.modernjava.common.Apple;

public class AppleHeavyWeightPredicate implements ApplePredicate {
    @Override
    public boolean test (Apple apple) {
        return apple.getWeight() >= 150;
    }
}
