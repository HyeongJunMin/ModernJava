package com.mj.modernjava.ch15;

public interface Subscriber<T> {
  void onSubscribe(Subscription subscription);
  void onNext(T t);
  void onError(Throwable throwable);
  void onComplete();
}
