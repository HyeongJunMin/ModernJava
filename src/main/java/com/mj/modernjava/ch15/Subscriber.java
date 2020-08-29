package com.mj.modernjava.ch15;

public interface Subscriber<T> {
  void onNext(T t);
}
