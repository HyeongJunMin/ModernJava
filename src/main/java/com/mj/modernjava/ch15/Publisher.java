package com.mj.modernjava.ch15;

public interface Publisher <T> {
  void subscribe(Subscriber<? super T> subscriber);
}
