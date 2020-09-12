package com.mj.modernjava.ch15;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class SimpleCell implements Publisher<Integer>, Subscriber<Integer>{
  private int value = 0;
  private String name;
  private List<Subscriber> subscribers = new ArrayList<>();
  public SimpleCell(String name) { this.name = name; }
  @Override
  public void subscribe(Subscriber subscriber) {
    subscribers.add(subscriber);
  }
  private void notifyAllSubscribers() { //새로운 값이 있음을 모든 구독자에게 알리는 메소드
    subscribers.forEach(subscriber -> subscriber.onNext(this.value));
  }
  @Override
  public void onNext(Integer newValue) {
    this.value = newValue;  //구독한 셀에서 새 값이 생겼을 때 값을 갱신해서 반응함
    log.info("name : {}, value : {}", this.name, this.value);
    notifyAllSubscribers();
  }

  @Override
  public void onSubscribe(Subscription subscription) {

  }

  @Override
  public void onError(Throwable throwable) {

  }

  @Override
  public void onComplete() {

  }
}
