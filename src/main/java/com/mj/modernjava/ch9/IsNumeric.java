package com.mj.modernjava.ch9;

public class IsNumeric implements ValidationStrategy {

  @Override
  public boolean execute(String s) {
    return s.matches("\\d+");
  }
}
