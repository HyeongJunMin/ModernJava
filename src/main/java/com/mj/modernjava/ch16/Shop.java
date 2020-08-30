package com.mj.modernjava.ch16;

import static com.mj.modernjava.ch16.Util.delay;
import static com.mj.modernjava.ch16.Util.format;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public class Shop {

  private final String name;
  private final Random random;

  public Shop(String name) {
    this.name = name;
    random = new Random(name.charAt(0) * name.charAt(1) * name.charAt(2));
  }

  public String getPrice(String product) {
    double price = calculatePrice(product);
    Discount.Code code = Discount.Code.values()[random.nextInt(Discount.Code.values().length)];
    return name + ":" + price + ":" + code;
  }

  public Future<Double> getPriceAsync(String product) {
//    CompletableFuture<Double> futurePrice = new CompletableFuture<>();
//    new Thread(() -> {
//      try {
//        double price = calculatePrice(product);
//        futurePrice.complete(price);
//      } catch (Exception e) {
//        futurePrice.completeExceptionally(e);
//      }
//    }).start();
//    return futurePrice;
    return CompletableFuture.supplyAsync(() -> calculatePrice(product));
  }

  public double calculatePrice(String product) {
    delay();
    return format(random.nextDouble() * product.charAt(0) + product.charAt(1));
  }

  public String getName() {
    return name;
  }

}