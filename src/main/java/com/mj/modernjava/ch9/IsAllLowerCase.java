package com.mj.modernjava.ch9;

public class IsAllLowerCase implements ValidationStrategy {

  @Override
  public boolean execute(String s) {
    return s.matches("[a-z]+");
  }
}
