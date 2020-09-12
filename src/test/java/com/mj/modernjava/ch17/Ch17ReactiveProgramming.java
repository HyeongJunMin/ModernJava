package com.mj.modernjava.ch17;

import com.mj.modernjava.ch15.Publisher;
import org.junit.jupiter.api.Test;

public class Ch17ReactiveProgramming {
  @Test
  void subscribe() {
    getTemperatures("New York").subscribe(new TempSubscriber());
    // temperature info : TempInfo(town=New York, temp=81)
    // temperature info : TempInfo(town=New York, temp=24)
    // temperature info : TempInfo(town=New York, temp=19)
    // temperature fetch error : Temperature Fetch Error!
  }
  private static Publisher<TempInfo> getTemperatures(String town) {
    return subscriber -> subscriber.onSubscribe(
        new TempSubscription(subscriber, town)
    );
  }
}
