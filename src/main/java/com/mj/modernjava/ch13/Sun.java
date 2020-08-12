package com.mj.modernjava.ch13;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Sun implements Moveable, Rotatable {
  private int x;
  private int y;
  private int rotationAngle;
}
