package com.mj.modernjava.ch19;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Getter @Setter
@ToString(exclude = "onWard")
public class TrainJourney {
  private String name;
  private int price;
  private TrainJourney onWard;

  public TrainJourney (String name) {
    this.name = name;
  }

  // 부작용이 발생하는 파괴적 갱신
  public static TrainJourney link(TrainJourney targetJourney, TrainJourney newJourney) {
    if (targetJourney == null) {
      return newJourney;
    }
    TrainJourney t = targetJourney;
    while(t.getOnWard() != null) {
      t = t.getOnWard();
    }
    t.setOnWard(newJourney);
    return targetJourney;
  }

  //부작용을 없앤 함수형 갱신
  public static TrainJourney append(TrainJourney targetJourney, TrainJourney newJourney) {
    return targetJourney == null ? newJourney : new TrainJourney(targetJourney.getName(), targetJourney.getPrice(), append(targetJourney.getOnWard(), newJourney));
  }

}
