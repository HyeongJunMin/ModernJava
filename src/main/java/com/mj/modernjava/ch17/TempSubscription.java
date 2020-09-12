package com.mj.modernjava.ch17;


import com.mj.modernjava.ch15.Subscriber;
import com.mj.modernjava.ch15.Subscription;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class TempSubscription implements Subscription {
  private final Subscriber<? super TempInfo> subscriber;
  private final String town;
  @Override
  public void request(long n) {
    for (long i = 0L; i < n; i++) { //Subscriber가 만든 요청을 한 개씩 반복
      try {
        subscriber.onNext(TempInfo.fetch(town));
      } catch (Exception e) {
        subscriber.onError(e);  //온도 가져오기 실패하면 Subscriber로 에러 전달
        break;
      }
    }
  }
  @Override
  public void cancel() {
    subscriber.onComplete();  //구독 취소되면 완료신호를 Subscriber로 전달
  }
}
