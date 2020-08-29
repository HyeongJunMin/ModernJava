package com.mj.modernjava.ch16;

public class Shop {

  public double getPrice(String product) {
    return calculatePrice(product);
  }

  private double calculatePrice(String product) {
    delay();
    return Math.random() * (product.charAt(0) + product.charAt(1));
  }

  public static void delay() {
    try {
      Thread.sleep(1000L);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }
}
