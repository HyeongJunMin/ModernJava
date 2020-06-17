package com.mj.modernjava.ch9;

public class LeMonde implements TweetObserver {

  @Override
  public void notify(String tweet) {
    if (tweet != null && tweet.contains("wine")) {
      System.out.println("Today cheese, wine and news! " + tweet);
    }
  }
}
