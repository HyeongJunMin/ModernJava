package com.mj.modernjava.ch16;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class Ch16CompletableFuture {

  List<Shop> shops = Arrays.asList(
      new Shop("BestPrice"),
      new Shop("LetsSaveBig"),
      new Shop("MyFavoriteShop0"),
      new Shop("MyFavoriteShop1"),
      new Shop("MyFavoriteShop2"),
      new Shop("MyFavoriteShop3"),
      new Shop("MyFavoriteShop4"),
      new Shop("MyFavoriteShop5"),
      new Shop("MyFavoriteShop6"),
      new Shop("MyFavoriteShop7"),
      new Shop("BuyItAll"));

  private final Executor executor = Executors.newFixedThreadPool(Math.min(shops.size(), 100), new ThreadFactory() {
    @Override
    public Thread newThread(Runnable r) {
      Thread thread = new Thread(r);
      thread.setDaemon(true);// 프로그램 종료를 방해하지 않는 데몬 스레드를 사용
      return thread;
    }
  });

  @Test
  public void availableProcessors() {
    log.info("availableProcessors : {}", Runtime.getRuntime().availableProcessors());
  }

  @Test
  public void asyncMethod() {
    Shop bestShop = new Shop("BestShop");
    long start = System.nanoTime();
    Future<Double> futurePrice = bestShop.getPriceAsync("my favorite product");
    long invocationTime = (System.nanoTime() - start) / 1_000_000;
    log.info("Invocation returned after {} msecs", invocationTime);
    try {
      double price = futurePrice.get();
      log.info("price : {}", price);
    } catch (Exception e) {
      throw new RuntimeException();
    }
    long retrievalTime = (System.nanoTime() - start) / 1_000_000;
    log.info("price returned after {} msecs", retrievalTime);
    // Invocation returned after 3 msecs
    // price : 123.26
    // price returned after 1009 msecs
  }

  @Test
  public void blockExample() {
    long start = System.nanoTime();
    findPrices("myPhone24").forEach(price -> log.info("price : {}", price));
    long duration = (System.nanoTime() - start) / 1_000_000;
    log.info("time : {}", duration);
  }
  private List<String> findPrices(String product) {
    return shops.stream()
        .map(shop -> String.format("%s price is %s", shop.getName(), shop.getPrice(product)))
        .collect(Collectors.toList());
  }

  @Test
  public void noneBlockByParallelStream() {
    long start = System.nanoTime();
    findPricesByParallelStream("myPhone24").forEach(price -> log.info("price : {}", price));
    long duration = (System.nanoTime() - start) / 1_000_000;
    log.info("time : {}", duration);
    // shop size == 11 : 2021ms
  }
  private List<String> findPricesByParallelStream(String product) {
    return shops.parallelStream()
        .map(shop -> String.format("%s price is %s", shop.getName(), shop.getPrice(product)))
        .collect(Collectors.toList());
  }

  @Test
  public void noneBlockBySupplyAsync() {
    long start = System.nanoTime();
    findPricesBySupplyAsync("myPhone24").forEach(price -> log.info("price : {}", price));
    long duration = (System.nanoTime() - start) / 1_000_000;
    log.info("time : {}", duration);
    // shop size == 11 : 2045ms
  }
  private List<String> findPricesBySupplyAsync(String product) {
    List<CompletableFuture<String>> priceFutures = shops.stream()
        .map(shop -> CompletableFuture.supplyAsync(
            () -> String.format("%s price is %s", shop.getName(), shop.getPrice(product))))
        .collect(Collectors.toList());
    return priceFutures.stream()
        .map(CompletableFuture::join)
        .collect(Collectors.toList());
  }

  @Test
  public void noneBlockBySupplyAsyncWithCustomExecutor() {
    long start = System.nanoTime();
    findPricesBySupplyAsyncWithCustomExecutor("myPhone24").forEach(price -> log.info("price : {}", price));
    long duration = (System.nanoTime() - start) / 1_000_000;
    log.info("time : {}", duration);
    // shop size == 11 : 1038ms
  }
  private List<String> findPricesBySupplyAsyncWithCustomExecutor(String product) {
    List<CompletableFuture<String>> priceFutures = shops.stream()
        .map(shop -> CompletableFuture.supplyAsync(
            () -> String.format("%s price is %s", shop.getName(), shop.getPrice(product)), executor))
        .collect(Collectors.toList());
    return priceFutures.stream()
        .map(CompletableFuture::join)
        .collect(Collectors.toList());
  }

  @Test
  public void findDiscountedPrice() {
    long start = System.nanoTime();
    findDiscountedPrice("myPhone24").forEach(price -> log.info("price : {}", price));
    long duration = (System.nanoTime() - start) / 1_000_000;
    log.info("time : {}", duration);
    // shop size == 11 : 22,086ms
  }
  private List<String> findDiscountedPrice(String product) {
    return shops.stream()
        .map(shop -> shop.getPrice(product))
        .map(Quote::parse)
        .map(Discount::applyDiscount)
        .collect(Collectors.toList());
  }

  @Test
  public void findDiscountedPriceWithCustomExecutor() {
    long start = System.nanoTime();
    findDiscountedPriceWithCustomExecutor("myPhone24").forEach(price -> log.info("price : {}", price));
    long duration = (System.nanoTime() - start) / 1_000_000;
    log.info("time : {}", duration);
    // shop size == 11 : 2,059ms
  }
  private List<String> findDiscountedPriceWithCustomExecutor(String product) {
    List<CompletableFuture<String>> collect = shops.stream()
        //가격 정보 얻기
        .map(shop -> CompletableFuture.supplyAsync(() -> shop.getPrice(product), executor))
        //Quote 파싱하기
        .map(future -> future.thenApply(Quote::parse))
        //CompletableFuture를 조합해서 할인된 가격 계산하기
        .map(future -> future.thenCompose(quote -> CompletableFuture.supplyAsync(() -> Discount.applyDiscount(quote), executor)))
        .collect(Collectors.toList());
    return collect.stream()
        .map(CompletableFuture::join)
        .collect(Collectors.toList());
  }
}
