package com.mj.modernjava.ch17;

import com.mj.modernjava.ch15.Publisher;
import com.mj.modernjava.ch15.Subscriber;
import io.reactivex.Observable;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import static java.util.stream.Collectors.toList;

@Slf4j
public class Ch17ReactiveProgramming {
  @Test
  void subscribe() {
    getTemperatures("New York").subscribe(new TempSubscriber());
    // temperature info : TempInfo(town=New York, temp=81)
    // temperature info : TempInfo(town=New York, temp=24)
    // temperature info : TempInfo(town=New York, temp=19)
    // temperature fetch error : Temperature Fetch Error!
    String town = "London";
    Publisher pub = new Publisher() {
      public void subscribe(Subscriber subscriber) {
        subscriber.onSubscribe(new TempSubscription(subscriber, town));
      }
    };
    pub.subscribe(new TempSubscriber());
  }
  private static Publisher<TempInfo> getTemperatures(String town) {
    return subscriber -> subscriber.onSubscribe(
        new TempSubscription(subscriber, town)
    );
  }

  @Test
  void subscribeWithProcessor() {
    getCelsiusTemperature("Hanoi").subscribe(new TempSubscriber());
    // temperature info : TempInfo(town=Hanoi, temp=26)
    // temperature info : TempInfo(town=Hanoi, temp=-7)
    // temperature info : TempInfo(town=Hanoi, temp=13)
    // temperature fetch error : Temperature Fetch Error!
  }
  private static Publisher<TempInfo> getCelsiusTemperature(String town) {
    return subscriber -> {
      // Processor를 만들어서 Subscribe와 Publisher 사이에 연결
      TempProcessor processor = new TempProcessor();
      processor.subscribe(subscriber);
      processor.onSubscribe(new TempSubscription(processor, town));
    };
  }

  @Test
  void observable() {
    // just() : 한 개 이상의 요소를 이용해 이를 방출하는 Observable로 변환
    Observable<String> strings = Observable.just("first", "second");
    //
    // interval() : 0에서 시작해 주어진 간격마다 값을 무한히 방출
    Observable<Long> onPerSec = Observable.interval(1L, TimeUnit.SECONDS);
    //
    onPerSec.blockingSubscribe(i -> log.info("result : {}", TempInfo.fetch("Seoul")));

  }

  @Test
  void observableCustom() {
    getTemperature("HongKong").blockingSubscribe(new TempObserver());
  }
  private static Observable<TempInfo> getTemperature(String town) {
    return Observable.create(emitter -> // Observer를 소비하는 함수로부터 Observable 만들기
            Observable.interval(1, TimeUnit.SECONDS) // 매 초마다 무한으로 증가하는 long값을 방출하는 Observable
              .subscribe(i -> {
                if(!emitter.isDisposed()) { // 소비된 Observer가 아직 폐기되지 않았으면 작업 수행
                  if (i >= 5) { // 5번 온도보고했으면
                    emitter.onComplete(); // 옵저버를 완료하고 스트림 종료
                  } else {
                    try {
                      emitter.onNext(TempInfo.fetch(town)); // 아니면 온도를 보고
                    } catch (Exception e) {
                      emitter.onError(e); // 에러 발생하면 Observer에 알림
                    }
                  }
                }
            })
    );
  }
  @Test
  void observableMap() {
    getCelsiusTemperatureInRxJava("Berlin").blockingSubscribe(new TempObserver());
    // tempinfo : TempInfo(town=Berlin, temp=4)
    // tempinfo : TempInfo(town=Berlin, temp=16)
    // tempinfo : TempInfo(town=Berlin, temp=22)
    // got problem : Temperature Fetch Error!
    getCelsiusTemperaturesInRxJava("Seoul", "Busan", "Incheon").blockingSubscribe(new TempObserver());
    // tempinfo : TempInfo(town=Incheon, temp=1)
    // tempinfo : TempInfo(town=Seoul, temp=-3)
    // tempinfo : TempInfo(town=Busan, temp=19)
    // tempinfo : TempInfo(town=Busan, temp=-5)
    // tempinfo : TempInfo(town=Seoul, temp=14)
    // got problem : Temperature Fetch Error!

  }
  private Observable<TempInfo> getCelsiusTemperatureInRxJava(String town) {
    return getTemperature(town)
            .filter(tempInfo -> tempInfo.getTemp() > 0)
            .map(temp -> new TempInfo(temp.getTown(), (temp.getTemp() - 32) * 5 / 9));
  }
  private Observable<TempInfo> getCelsiusTemperaturesInRxJava(String... towns) {
    return Observable.merge(Arrays.stream(towns)
            .map(TempObservable::getCelsiusTemperature)
            .collect(toList()));
  }
}
