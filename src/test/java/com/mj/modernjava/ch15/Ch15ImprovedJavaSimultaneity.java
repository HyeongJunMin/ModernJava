package com.mj.modernjava.ch15;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class Ch15ImprovedJavaSimultaneity {

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
}
