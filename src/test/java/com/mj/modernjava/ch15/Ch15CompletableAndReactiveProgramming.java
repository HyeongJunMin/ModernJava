package com.mj.modernjava.ch15;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.concurrent.*;
import java.util.function.IntConsumer;

@Slf4j
public class Ch15CompletableAndReactiveProgramming {

  @Test
  public void makeThreadPool() {
    // 워커 스레드라 불리는 nThreads를 포함하는 ExecutorService를 만들고 이들을 스레드 풀에 저장한다.
    // 스레드 풀에서 사용하지 않은 스레드를 가지고, 제출된 태스크를 먼저 온 순서대로 실행한다.
    // 이들 태스크 실행이 종료되면 사용했던 스레드를 스레드풀로 반환한다.
    // 장점 : 하드웨어에 맞는 수의 태스크를 유지함과 동시에 수 천개의 태스크를 스레드 풀에 아무 오버헤드 없이 제출할 수 있다.
    // 큐의 크기 조정, 거부 정책, 태스크 종류에 따른 우선순위 등 다양한 설정을 할 수 있다.
    ExecutorService executorService = Executors.newFixedThreadPool(1);
    log.info("executorService : {}", executorService.toString());
    //executorService : java.util.concurrent.ThreadPoolExecutor@51dcb805[Running, pool size = 0, active threads = 0, queued tasks = 0, completed tasks = 0]
  }

  @Test
  public void changeFromThreadToThreadPool() throws InterruptedException, ExecutionException {
    int x = 1337;
    Result result = new Result();
    //by Thread
    Thread thread1 = new Thread(() -> result.left = f(x));
    Thread thread2 = new Thread(() -> result.right = g(x));
    thread1.start();
    thread2.start();
    thread1.join();
    thread2.join();
    log.info("left : {}, right : {}, sum : {}", result.left, result.right, result.left + result.right);
    //by ThreadPool with ExecutorService
    ExecutorService executorService = Executors.newFixedThreadPool(2);
    Future<Integer> y = executorService.submit(() -> f(x));
    Future<Integer> z = executorService.submit(() -> g(x));
    log.info("y : {}, z : {}, sum : {}", y.get(), z.get(), y.get() + z.get());
  }
  @Getter
  @Setter
  private static class Result {
    private int left;
    private int right;
  }
  private static int f(int x) {
    log.info("execute heavy work function f(x)");
    return x * 10;
  }
  private static int g(int x) {
    log.info("execute heavy work function g(x)");
    return x + 10;
  }

  @Test
  public void reactiveProgramming() {
    int x = 1337;
    Result result = new Result();
    f(x, (int y) -> {
      result.left = y;
      log.info("sum : {}", result.getLeft() + result.getRight());
    });
    g(x, (int z) -> {
      result.right = z;
      log.info("sum : {}", result.getLeft() + result.getRight());
    });
  }
  private void f(int x, IntConsumer dealWithResult) {
    log.info("reactive programming");
    dealWithResult.accept(Functions.f(x));
  }
  private void g(int x, IntConsumer dealWithResult) {
    log.info("reactive programming");
    dealWithResult.accept(Functions.g(x));
  }

  @Test
  public void cfComplete() throws ExecutionException, InterruptedException {
    //f(x)의 실행이 끝나지 않으면 get()을 기다려야 하므로 프로세싱 자원을 낭비할 수 있다.
    ExecutorService executorService = Executors.newFixedThreadPool(10);
    int x = 1337;
    CompletableFuture<Integer> completableFuture = new CompletableFuture<>();
    executorService.submit(() -> completableFuture.complete(f(x)));
    int b = g(x);
    log.info("result : {}", completableFuture.get() + b); //result : 14717
    executorService.shutdown();
  }

  @Test
  public void cfCompleteWithCombine() throws ExecutionException, InterruptedException {
    ExecutorService executorService = Executors.newFixedThreadPool(10);
    int x = 1337;
    CompletableFuture<Integer> cfF = new CompletableFuture<>();
    CompletableFuture<Integer> cfG = new CompletableFuture<>();
    CompletableFuture<Integer> cfCombine = cfF.thenCombine(cfG, (y, z) -> y + z);
    executorService.submit(() -> cfF.complete(f(x)));
    executorService.submit(() -> cfG.complete(g(x)));
    log.info("result : {}", cfCombine.get()); //result : 14717
    executorService.shutdown();
  }

  @Test
  public void sumFlows() {
    SimpleCell c1 = new SimpleCell("C1");
    SimpleCell c2 = new SimpleCell("C2");
    ArithmeticCell c3 = new ArithmeticCell("C3");
    c1.subscribe(c3::setLeft);
    c2.subscribe(c3::setRight);
    c1.onNext(10);
    c2.onNext(20);
    c1.onNext(15);
    // name : C1, value : 10
    // name : C3, value : 10
    // name : C2, value : 20
    // name : C3, value : 30
    // name : C1, value : 15
    // name : C3, value : 35
  }

}
