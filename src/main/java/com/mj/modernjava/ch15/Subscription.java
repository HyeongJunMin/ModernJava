package com.mj.modernjava.ch15;

public interface Subscription {
  public void request(long n);
  public void cancel();
}
