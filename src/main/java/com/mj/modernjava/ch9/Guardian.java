package com.mj.modernjava.ch9;

public class Guardian implements TweetObserver {

  @Override
  public void notify(String tweet) {
    if (tweet != null && tweet.contains("queen")) {
      System.out.println("Yet more news from London... " + tweet);
    }
  }
}
