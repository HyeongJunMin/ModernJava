package com.mj.modernjava.ch13;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Monster implements Moveable, Resizable, Rotatable{
  private int x;
  private int y;
  private int rotationAngle;
  private int width;
  private int height;
  public void setAbsoluteSize(int width, int height) {
    this.width = width;
    this.height = height;
  }
}
