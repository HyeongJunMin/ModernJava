package ch2;

import common.Apple;

public class AppleHeavyWeightPredicate implements ApplePredicate {
    public boolean test (Apple apple) {
        return apple.getWeight() >= 150;
    }
}
