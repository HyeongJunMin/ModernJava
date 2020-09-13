package com.mj.modernjava.ch17;


import com.mj.modernjava.ch15.Subscriber;
import com.mj.modernjava.ch15.Subscription;
import lombok.AllArgsConstructor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@AllArgsConstructor
public class TempSubscription implements Subscription {
  // request메서드가 TempSubscription자신에게 또 다른 요소를 보내는 재귀호출을 방지하기 위해 스레드 컨트롤
  // 예제에서는 Subscriber의 onError가 예외를 발생시키기 때문에 없어도 됨
  private static final ExecutorService executor = Executors.newSingleThreadExecutor();
  private final Subscriber<? super TempInfo> subscriber;
  private final String town;
  @Override
  public void request(long n) {
    executor.submit(() -> { // 다른 스레드에서 다음 요소를 구독자에게 보냄
      for (long i = 0L; i < n; i++) { //Subscriber가 만든 요청을 한 개씩 반복
        try {
          subscriber.onNext(TempInfo.fetch(town));
        } catch (Exception e) {
          subscriber.onError(e);  //온도 가져오기 실패하면 Subscriber로 에러 전달
          break;
        }
      }
    });
  }
  @Override
  public void cancel() {
    subscriber.onComplete();  //구독 취소되면 완료신호를 Subscriber로 전달
  }
}
