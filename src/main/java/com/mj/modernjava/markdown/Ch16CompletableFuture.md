# 목차

> 요약 및 결론  
> 책 내용  

---

#### 요약 및 결론
- 예제가 재미있었다
- Async만 알았던 비동기.. 쉽지않다
#### 책 내용
1. Future의 단순 활용
    - Java5부터 생긴 Future
        - 미래의 어느 시점에 결과를 얻는 모델에 활용
        - 작업을 Callable객체 내부로 감싼 다음 ExecutorService에 제출해야 한다.
        - 오래 걸리는 작업이 영원히 끝나지 않으면? 타임아웃 설정을 해주면 되긴 하는데.. future.get(1, TimeUnit.SECONDS);
    1. Future 한계
        - Future가 제공하는 메서드 : 비동기 계산이 끝났는지 확인, 계산이 끝나길 기다림, 결과 회수
        - 복잡한 동작을 구현하기에는 쉽지 않음
        - CompletableFuture의 기능들 (Future만 있었을 때 필요했던 기능들)
            - 두 개의 비동기 계산 결과를 하나로 합친다.
            - Future 집합이 실행하는 모든 태스트의 완료를 기다린다.
            - Future 집합에서 가장 빨리 완료되는 태스크를 기다렸다가 결과를 얻는다.
            - 프로그램적으로 Future를 완성시킨다(비동기 동작에 수동으로 결과 제공)
            - Future 완료 동작에 바응(결과를 기다리며 블록되지 않고 결과가 준비되었다는 알림을 받은 다음 추가 동작 수행 가능)
    2. CompletableFuture로 비동기 애플리케이션 만들기
        - 가장 저렴한 가격을 제시하는 상점을 찾는 애플리케이션을 완성해가는 예제
        - 배울 수 있는 것들
            1. 비동기 API를 제공하는 방법
            2. 동기 API를 사용할 때 코드를 비블록으로 만드는 방법
            3. 비동기 동작의 완료에 대응하는 방법
2. 비동기 API 구현
    - 예제 설정 : https://github.com/HyeongJunMin/ModernJava/blob/master/src/main/java/com/mj/modernjava/ch16/Shop.java 
    1. 동기 메서드를 비동기 메서드로 변환
        - 변환은 했지만 Future의 값을 얻기 까지 블록됐음. 블록을 거의 완벽하게 회피하는 방법은 나중에
        ```
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
        ```
    2. 에러 처리 방법
        - 예외는 해당 스레드에만 영향을 미치기 때문에 에러를 전파해줄 필요가 있다.
        - CompletableFuture에 completeExceptionally(Exception e)를 활용한다.
        ```
        public Future<Double> getPriceAsync(String product) {
          CompletableFuture<Double> futurePrice = new CompletableFuture<>();
          new Thread(() -> {
            try {
              double price = calculatePrice(product);
              futurePrice.complete(price);
            } catch (Exception e) {
              futurePrice.completeExceptionally(e);
            }
          }).start();
          return futurePrice;
        }
        ```
    3. 팩토리 메서드 supplyAsync로 CompletableFuture 만들기
        - 위에 에러처리 방법 까지 모두 포함돼있음
        ```
        public Future<Double> getPriceAsync(String product) {
          return CompletableFuture.supplyAsync(() -> calculatePrice(product));
        }
        ```
3. 비블록 코드 만들기
    - 위 예제코드를 외부 API라고 가정
    - 4개의 상점에서 각각의 가격을 불러오는 블록 코드
    - getPrice()마다 1초가 걸리니까 성능이 아주 좋지 못함(4022ms 소)
    ```
    List<Shop> shops = Arrays.asList(
        new Shop("BestPrice"),
        new Shop("LetsSaveBig"),
        new Shop("MyFavoriteShop"),
        new Shop("BuyItAll"));
    @Test
    public void blockExample() {
     long start = System.nanoTime();
     findPrices("myPhone24").forEach(price -> log.info("price : {}", price));
     long duration = (System.nanoTime() - start) / 1_000_000;
     log.info("time : {}", duration); // time : 4022
    }
    private List<String> findPrices(String product) {
     return shops.stream()
         .map(shop -> String.format("%s price is %s", shop.getName(), shop.getPrice(product)))
         .collect(Collectors.toList());
    }
    ```
    1. 병렬 스트림으로 요청 병렬화하기
        - 병렬 스트림 활용
        - 위 블록 코드에 비해 성능 크게 향상(1015ms 소요)
        ```
        @Test
        public void noneBlockByParallelStream() {
          long start = System.nanoTime();
          findPricesByParallelStream("myPhone24").forEach(price -> log.info("price : {}", price));
          long duration = (System.nanoTime() - start) / 1_000_000;
          log.info("time : {}", duration);  // time : 1015
        }
        private List<String> findPricesByParallelStream(String product) {
          return shops.parallelStream()
              .map(shop -> String.format("%s price is %s", shop.getName(), shop.getPrice(product)))
              .collect(Collectors.toList());
        }
        ```
    2. CompletableFuture로 비동기 호출 구현하기
        - 스트림 연산은 게으른 특성이 있기 때문에 두 개의 파이프라인으로 연산을 나누어 처리
        - CompletableFuture로 각 상점의 정보를 요청할 때 기존 요청 작업이 완료돼야 join이 결과를 반환하면서 다음 상점으로 정보를 요청할 수 있기 때문
        ```
        @Test
        public void noneBlockBySupplyAsync() {
          long start = System.nanoTime();
          findPricesBySupplyAsync("myPhone24").forEach(price -> log.info("price : {}", price));
          long duration = (System.nanoTime() - start) / 1_000_000;
          log.info("time : {}", duration);
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
        ```
    3. 더 확장성이 좋은 해결 방법
        - 병렬 스트림, CompletableFuture 둘 다 결과가 비슷
            - 내부적으로 Runtime.getRuntime().availableProcessors()가 반환하는 스레드 수를 사용하기 때문
        - 그러나 CompletableFuture는 병렬 스트림에 비해 작업에 이용할 수 있는 다양한 Executor 지정 가능
    4. 커스텀 Executor 사용하기
        - 스레드 풀이 관리하는 스레드 수는 어떻게 결정해야 하나?
            - 낭비 되지 않게끔 적절하게
            - 예제에서는 상점 수 만큼 스레드 수를 설정하고 일반 스레드와 성능이 같지만 자바 프로그램이 종로될 때 강제로 종료될 수 있는 데몬스레드 사용
            ```
            private final Executor executor = Executors.newFixedThreadPool(Math.min(shops.size(), 100), new ThreadFactory() {
              @Override
              public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setDaemon(true);// 프로그램 종료를 방해하지 않는 데몬 스레드를 사용
                return thread;
              }
            });
            ```
        - executor를 지정하면 성능이 2배임을 확인
        - 애플리케이션의 특성에 맞는 Executor를 만들어서 CompletableFuture를 활용하는 것이 바람직하다
    5. 그럼 어떻게 써야되나요?
        - I/O가 포함되지 않은 계산 중심의 동작을 실행할 때는 스트림 인터페이스가 가장 구현하기 간단하며 효율적일 수 있다.
        - I/O를 기다리는 작업을 병렬로 실행할 때는 CompletableFuture가 더 많은 유연성을 제공한다. 스트림의 게으른 특성 때문에 I/O를 실제로 언제 처리할지 예측하기 어려운 문제도 있다.
4. 비동기 작업 파이프라인 만들기
    - 우리와 계약을 맺은 모든 상점이 하나의 할인 서비스를 사용한다고 가정
    1. 할인 서비스 구현
        - Quote class : 상점에서 제공한 문자열(상점이름:가격:할인코드) 파싱
        - Discount class : 할인코드 정의, 할인코드에 맞는 금액 계산, 할인 서비스 응답 지연 
    2. 할인 서비스 사용
        - 동기 작업으로 하면 끔찍하게 느림(테스트 코드 확인 Ch16CompletableFuture.findDiscountedPrice())
    3. 동기 작업과 비동기 작업 조합하기
        ```
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
        ```
        - 가격 정보 얻기
            - 변환 결과 : Stream<CompletableFuture<String>>
            - 각 CompletableFuture는 작업이 끝났을 때 해당 상점이 반환하는 문자열 정보를 포함한다.
            - 그리고 커스텀 Executor를 설정해줬다
        - Quote 파싱하기
            - 얻어 온 가격정보를 Quote로 변환한다.
            - thenApply메서드는 CompletableFuture가 끝날 때 까지 블록하지 않기 때문에 동작을 완전히 완료한 다음에 thenApply메서드로 전달된 람다 표현식을 적용한다.
            - 따라서 CompletableFuture<String>를 CompletableFuture<Quote>로 변환해준다.
        - CompletableFuture를 조합해서 할인된 가격 계산하기
            - 정상 가격에 Discount 서비스에서 제공하는 할인율을 적용할 차례
            - 두 가지 CompletableFuture로 이루어진 연쇄적으로 수행되는 두 개의 비동기 동작을 작성
                1. 상점에서 가격 정보를 얻어와서 Quote로 변환
                2. 변환된 Quote를 Discount서비스로 전달해서 할인된 최종가격 획득
            - thenCompose : 두 비동기 연산을 파이프라인으로 만들어 주는 역할
                - 첫 번째 연산의 결과를 두 번째 연산으로 전달해준다.
                - 첫 번째 CompletableFuture에 thenCompose를 호출하고 Function에 넘겨주는 방법
                - Function은 첫 번째 CompletableFuture에 반환 결과를 인수로 받고 두 번째 CompletableFuture를 반환한다
                - 두 번째 CompletableFuture는 첫 번째 CompletableFuture의 결과를 계산의 입력으로 사용한다.
                - 따라서 Future가 여러 상점에서 Quote를 얻는 동안 메인 스레드는 UI 이벤트에 반응하는 등 유용한 작업을 수행할 수 있다.
    4. 독립 CompletableFuture와 비독립 CompletableFuture 합치기
        - 위 예제는 첫 번째 CompletableFuture에 thenCompose메서드를 실행한 다음에 실행 결과를 두 번째 CompletableFuture로 전달했다
        - 그런데 독립적으로 실행된 두 개의 CompletableFuture 결과를 합쳐야 하는 상황이 발생한다.
        - 물론 첫 번째 CompletableFuture의 동작 완료와 관계 없이 두 번째 CompletableFuture를 실행할 수 있어야 한다.
        - 이런 상황에서는 thenCombine 메서드를 사용한다.
            - BiFunction을 두 번째 인수로 받는다.
            - BiFunction은 두 개의 CompletableFuture 결과를 어떻게 합칠 지 정의한다.
            - Async 버전이 존재한다.
            - thenCombineAsync 메서드에서는 BiFunction이 정의하는 조합 동작이 스레드풀로 제출되면서 별도의 태스크에서 비동기적으로 수행된다. 
    5. Future의 리플렉션과 CompletableFuture의 리플렉션
        - CompletableFuture는 람다 표현식을 사용한다
            - 다양한 동기 태스크, 비동기 태스크를 활용해서 복잡한 연산 수행 방법을 효과적으로 쉽게 정의할 수 있는 선언형 API를 만들 수 있다.
    6. 타임아웃 효과적으로 사용하기(Java9)
        - Future의 계산 결과를 읽을 때는 무한정 기다리는 상황이 발생할 수 있으므로 블록을 하지 않는 것이 좋다.
        - Java9에 추가된 메서드를 활용해서 문제를 해결할 수 있다.
        - orTimeout : Future가 3초 후에도 작업을 못끝내면 TimeoutException 발생
        - completeOnTimeout : 1초 안에 작업이 끝나지 않으면 ExchangeService.DEFAULT_RATE을 사용
        ```
        @Test
        public void orTimeOut() {
          long start = System.nanoTime();
          findPricesInUSD("myPhone24").forEach(price -> log.info("price : {}", price));
          long duration = (System.nanoTime() - start) / 1_000_000;
          log.info("time : {}", duration);
        }
        private List<String> findPricesInUSD(String product) {
          shops.forEach(shop -> {
            // 아래 CompletableFuture::join와 호환되도록 futurePriceInUSD의 형식만 CompletableFuture로 바꿈.
            CompletableFuture<Double> futurePriceInUSD =
                    CompletableFuture.supplyAsync(() -> shop.getPrice(product))
                            .thenCombine(
                                    CompletableFuture.supplyAsync(
                                            () ->  ExchangeService.getRate(ExchangeService.Money.EUR, ExchangeService.Money.USD))
                                            // 자바 9에 추가된 타임아웃 관리 기능
                                            .completeOnTimeout(ExchangeService.DEFAULT_RATE, 1, TimeUnit.SECONDS),
                                    (price, rate) -> price * rate
                            )
                            // 자바 9에 추가된 타임아웃 관리 기능
                            .orTimeout(3, TimeUnit.SECONDS);
            priceFutures.add(futurePriceInUSD);
          });
        }
        ```
5. CompletableFuture의 종료에 대응하는 방법
    - 지금까지 예제에서 응답 지연은 1초로 고정시켰지만 실제 기능들은 그렇지 않을 가능성이 높다
    - 0.5초 ~ 2.5초 사이로 응답시간을 랜덤하게 설정한다.
    1. 최저가격 검색 애플리케이션 리팩터링
        - 먼저 모든 가격 정보를 포함할 때까지 리스트 생성을 기다리지 않도록 프로그램을 고쳐야 한다.
        - 그러려면 CompletableFuture의 스트림을 직접 제어해야 한다.
        ```
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
        ```
        - thenAccept : 연산 결과를 소비하는 Consumer를 인수로 받음
        - thenAcceptAsync : CompletableFuture가 완료된 스레드가 아니라 새로운 스레드를 이용해서 Consumer를 실행한다.
        - allOf : 배열을 받아서 CompletableFuture<Void>를 반환한다.
            - 원래 스트림의 모든 CompletableFuture의 실행 완료를 기다릴 수 있다.
    2. 응용
        - 위 예제에 각각 상점마다 수행시간을 확인
        ```
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
        ```
6. 로드맵
    - 다음 17장에서는 CompletableFuture의 기능이 한 번에 종료되지 않고 일련의 값을 생산하도록 일반화하는 Java9 플로 API를 살펴본다.