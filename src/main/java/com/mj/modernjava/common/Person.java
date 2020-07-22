package com.mj.modernjava.common;

import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@Builder
@Slf4j
public class Person {
  private Car car;
  private String name;

  public Optional<Car> getCar() {
    return Optional.ofNullable(this.car);
  }

  public static Person newInstance(String num) {
    log.info("num : {}", num);
    return new Person();
  }
}
