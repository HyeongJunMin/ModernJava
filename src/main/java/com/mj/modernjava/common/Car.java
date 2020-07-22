package com.mj.modernjava.common;

import java.io.Serializable;
import java.util.Optional;
import java.util.OptionalInt;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Car implements Serializable {

  private static final long serialVersionUID = 5364478132129145933L;

  private Insurance insurance;
  private String name;
  private OptionalInt distance;

  public Optional<Insurance> getInsurance() {
    return Optional.ofNullable(this.insurance);
  }
}
