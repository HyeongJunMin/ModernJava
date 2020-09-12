package com.mj.modernjava.ch17;

import com.mj.modernjava.ch15.Subscriber;
import com.mj.modernjava.ch15.Subscription;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TempSubscriber implements Subscriber<TempInfo> {
  private Subscription subscription;
  @Override
  public void onSubscribe(Subscription subscription) {
    // 구독을 저장하고 첫 번째 요청을 전달
    this.subscription = subscription;
    subscription.request(1L);
  }
  @Override
  public void onNext(TempInfo tempInfo) {
    // 수신한 온도를 출력하고 다음 정보를 요청
    log.info("temperature info : {}", tempInfo);
    subscription.request(1L);
  }
  @Override
  public void onError(Throwable throwable) {
    log.error("temperature fetch error : {}", throwable.getMessage());
  }
  @Override
  public void onComplete() {
    log.info("done!");
  }
}
