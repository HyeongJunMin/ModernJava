package com.mj.modernjava.ch16;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

//  @Test
//  public void orTimeOut() {
//    long start = System.nanoTime();
//    findPricesInUSD("myPhone24").forEach(price -> log.info("price : {}", price));
//    long duration = (System.nanoTime() - start) / 1_000_000;
//    log.info("time : {}", duration);
//  }
//  private List<String> findPricesInUSD(String product) {
//    shops.forEach(shop -> {
//      // 예제 10-20 시작.
//      // 아래 CompletableFuture::join와 호환되도록 futurePriceInUSD의 형식만 CompletableFuture로 바꿈.
//      CompletableFuture<Double> futurePriceInUSD =
//              CompletableFuture.supplyAsync(() -> shop.getPrice(product))
//                      .thenCombine(
//                              CompletableFuture.supplyAsync(
//                                      () ->  ExchangeService.getRate(ExchangeService.Money.EUR, ExchangeService.Money.USD))
//                                      // 자바 9에 추가된 타임아웃 관리 기능
//                                      .completeOnTimeout(ExchangeService.DEFAULT_RATE, 1, TimeUnit.SECONDS),
//                              (price, rate) -> price * rate
//                      )
//                      // 자바 9에 추가된 타임아웃 관리 기능
//                      .orTimeout(3, TimeUnit.SECONDS);
//      priceFutures.add(futurePriceInUSD);
//    });
//  }

  @Test
  public void findPricesStream() {
    long start = System.nanoTime();
    findPricesStream("myPhone").map(f -> f.thenAccept(System.out::println));
    long duration = (System.nanoTime() - start) / 1_000_000;
    log.info("time : {}", duration);
    // shop size == 11 : 4ms
    CompletableFuture[] futures = findPricesStream("myPhone").map(f -> f.thenAccept(System.out::println)).toArray(size -> new CompletableFuture[size]);
    CompletableFuture.allOf(futures).join();
  }
  private Stream<CompletableFuture<String>> findPricesStream(String product) {
    return shops.stream()
            .map(shop -> CompletableFuture.supplyAsync(() -> shop.getPriceRandomDelayed(product), executor))
            .map(future -> future.thenApply(Quote::parse))
            .map(future -> future.thenCompose(quote -> CompletableFuture.supplyAsync(() -> Discount.applyDiscount(quote), executor)));
  }

  @Test
  public void findPricesStreamApply() {
    long start = System.nanoTime();
    CompletableFuture[] futures = findPricesStream("myPhone")
            .map(f -> f.thenAccept(s -> log.info("{} done in {} msecs", s, (System.nanoTime() - start) / 1_000_000)))
            .toArray(size -> new CompletableFuture[size]);
    long duration = (System.nanoTime() - start) / 1_000_000;
    CompletableFuture.allOf(futures).join();
    log.info("All shops have now responded in : {}", duration);
    // MyFavoriteShop7 price is 192.72 done in 1579 msecs
    // MyFavoriteShop2 price is 192.72 done in 1993 msecs
    // MyFavoriteShop4 price is 192.72 done in 2036 msecs
    // MyFavoriteShop6 price is 192.72 done in 2264 msecs
    // MyFavoriteShop1 price is 192.72 done in 2399 msecs
    // MyFavoriteShop0 price is 192.72 done in 2598 msecs
    // BuyItAll price is 184.74 done in 2623 msecs
    // BestPrice price is 110.93 done in 2907 msecs
    // LetsSaveBig price is 135.58 done in 3060 msecs
    // MyFavoriteShop5 price is 192.72 done in 3307 msecs
    // MyFavoriteShop3 price is 192.72 done in 3503 msecs
    // All shops have now responded in : 35
  }
}
