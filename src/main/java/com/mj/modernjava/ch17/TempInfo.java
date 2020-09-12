package com.mj.modernjava.ch17;

import java.util.Random;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

// 현재 보고된 온도를 전달하는 자바 빈
@Getter
@AllArgsConstructor
@ToString
public class TempInfo {
  public static final Random random = new Random();
  private final String town;
  private final int temp;
  public static TempInfo fetch(String town) {
    if (random.nextInt(10) == 0) {
      throw new RuntimeException("Temperature Fetch Error!"); //10% 확률로 작업 실패
    }
    return new TempInfo(town, random.nextInt(100));
  }
}
