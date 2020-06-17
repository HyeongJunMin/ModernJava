package com.mj.modernjava.ch9;

public interface Subject {
  void registerObserver(TweetObserver o);
  void notifyObservers(String tweet);
}
