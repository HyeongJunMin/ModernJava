package com.mj.modernjava.ch9;

import java.util.ArrayList;
import java.util.List;

public class Feed implements Subject {

  private final List<TweetObserver> observers = new ArrayList<>();

  @Override
  public void registerObserver(TweetObserver o) {
    this.observers.add(o);
  }

  @Override
  public void notifyObservers(String tweet) {
    observers.forEach(o -> o.notify(tweet));
  }
}
