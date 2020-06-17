package com.mj.modernjava.ch9;

public class NYTimes implements TweetObserver {

  @Override
  public void notify(String tweet) {
    if (tweet != null && tweet.contains("money")) {
      System.out.println("Breaking news in NY! " + tweet);
    }
  }
}
