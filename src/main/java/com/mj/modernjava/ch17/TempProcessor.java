package com.mj.modernjava.ch17;

import com.mj.modernjava.ch15.Processor;
import com.mj.modernjava.ch15.Subscriber;
import com.mj.modernjava.ch15.Subscription;

public class TempProcessor implements Processor<TempInfo, TempInfo> {
  private Subscriber<? super TempInfo> subscriber;
  @Override
  public void subscribe(Subscriber<? super TempInfo> subscriber) {
    this.subscriber = subscriber;
  }
  @Override
  public void onNext(TempInfo tempInfo) {
    // 온도를 섭씨로 변환한 값으로 TempInfo를 만들어 다시 전송
    subscriber.onNext(new TempInfo(tempInfo.getTown(), (tempInfo.getTemp() - 32) * 5 / 9));
  }
  // 다른 모든 신호는 업스트림 구독자에게 전달
  @Override
  public void onSubscribe(Subscription subscription) { subscriber.onSubscribe(subscription); }
  @Override
  public void onError(Throwable throwable) { subscriber.onError(throwable); }
  @Override
  public void onComplete() { subscriber.onComplete(); }
}
