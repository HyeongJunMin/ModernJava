package com.mj.modernjava.ch15;

public class ArithmeticCell extends SimpleCell{
  private int left;
  private int right;
  public ArithmeticCell(String name) {
    super(name);
  }
  public void setLeft(Object left) {
    if (left instanceof Integer) {
      int leftValue = (Integer) left;
      this.left = leftValue;
      onNext(leftValue + this.right);
    }
  }
  public void setRight(Object right) {
    if (right instanceof Integer) {
      int rightValue = (Integer) right;
      this.right = rightValue;
      onNext(rightValue + this.left);
    }
  }
}
