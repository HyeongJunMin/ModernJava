# 목차

> 요약 및 결론  
> 책 내용  

---

#### 요약 및 결론
- 자바의 동시성 지원은 계속 진화해 가고 있는데 나는 잘 모르겠다
- Future는 CompletableFuture 없이는 쓰면 안될거라고 생각한다. get()호출했다가 영영 안끝나면 어떡해
- pub-sub 예제가 아주 좋다. 값셀 - 연산결과셀
#### 책 내용
CompletableFuture와 리액티브 프로그래밍 컨셉의 기초
> 소프트웨어 개발 방법을 획기적으로 뒤집는 두 가지 추세        
> 1. 하드웨어 관련
> 2. 애플리케이션 구성 관련
>   - MSA가 유행하면서 서비스가 작아진 대신 네트워크 통신 증가(매시업 애플리케이션)
>   - 매시업 애플리케이션에서 하나의 서비스의 응답을 기다리는 동안 다른 서비스를 처리하지 않을 이유가 없다.
>   - Java는이런 환경에서 사용할 수 있는 두 가지 주요 도구를 제공한다.
>       1. Future 인터페이스
>       2. 플로 API

1. 동시성을 구현하는 자바 지원의 진화
    - 최초 : Runnable과 Thread를 동기화된 클래스와 메서드를 이용해 잠금
    - Java 5 : 
        1. ExecuterService 인터페이스 : Executor인터페이스를 상속받으며 Callable을 실행하는 submit 메서드를 포함
        2. Runnable과 Thread의 변형을 반환하는 Callable<T>, Future<T>, 제네릭 지원
    - Java 7 : 분할정복 알고리즘의 포크/조인 구현을 지원하는 RecursiveTask 추가
    - Java 8 : 스트림과 새로 추가된 람다 지원에 기반한 병렬 프로세싱 추가
    - Java 9 : 분산 비동기 프로그래밍을 명시적으로 지원 
        - 리액티브 프로그래밍
        - 발행-구독 프로토콜로 이를 지원
    1. 스레드와 높은 수준의 추상화
        - 숫자 1,000,000개 배열을 처리하는 예제
        ```
        int[] stats = {1, 2, ... , 1_000_000};
        //네 개의 스레드를 만들어 계산
        long sum0 = 0;
        for(int i = 0 ; i < 250_000; i++) {
          sum0 += stats[i];
        }
        //내부반복을 통해 병렬성을 달성(스레드 사용 패턴 추상화)
        Arrays.stream(stats).parallel().sum();
        ```
    2. Executor와 스레드 풀
        - Java 5는 프레임워크와 스레드 풀을 통해 프로그래머가 태스크 제출과 실행을 분리할 수 있는 기능을 제공했다.
        - 스레드의 문제
            - 자바 스레드는 직접 운영체제 스레드에 접근한다.
            - 운영체제가 지원하는 스레드 수를 초과해 사용하면 예상치 못한 방식으로 크래시 될 수 있다.
        - 스레드 풀 그리고 스레드 풀이 더 좋은 이유
            - 자바 ExecutorService는 태스크를 제출하고 나중에 결과를 수집할 수 있는 인터페이스를 제공한다.
            - 프로그램은 newFixedThreadPool같은 팩토리 메서드 중 하나를 이용해 스레드 풀을 만들어 사용할 수 있다.
            ```
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
            ```
        - 스레드 풀 그리고 스레드 풀이 나쁜 이유
            - 거의 모든 관점에서 스레드를 직접 사용하는 것보다 스레드 풀을 이용하는 것이 바람직하다.
            - 그러나 두 가지 사항을 주의해야 한다.
                1. 블록(자거나 이벤트를 기다리는)할 수 있는 태스크는 스레드풀에 제출하지 말아야 한다.
                2. 프로그램을 종료하기 전에 모든 스레드 풀을 종료하는 습관을 갖는 것이 중요하다.
            - 별로 안나빠 보이는뎅
    3. 스레드의 다른 추상화 : 중첩되지 않은 메서드 호출
        - 엄격한 포크/조인
            - 태스크나 스레드가 메서드 호출 안에서 시작되면 그 메서드 호출은 반환하지 않고 작업이 끝나기를 기다린다.
            - 스레드 생성과 join()이 한 쌍처럼 중첩된 메서드 호출 내에 추가되는 방식
            - 병렬 스트림과 포크/조인 프레임워크의 동시성
        - 여유로운 포크/조인(비동기 메서드)
            - 메서드가 반환된 후에도 만들어진 태스크 실행이 계속되는 메서드
        - 그래서 뭐?
    4. 스레드에 무엇을 바라는가?
        - 일반적으로 프로그램을 작은 태스크 단위로 구조화하는 것이 목표이다.
        - 스레드를 조작하는 복잡한 코드를 구현하지 않고 메서드를 호출하는 방법을 살펴보자.
2. 동기 API와 비동기 API
    - Java 8 스트림을 이용해 명시적으로 병렬 하드웨어를 이용하는 방법을 이미 배웠다.
        - 외부 반복을 내부 반복으로 바꾸고, 스트림에 parallel()메서드를 이용한다.
    - 서로 상호작용하지 않는 두 작업 f(x)와 g(x)의 합을 효율적으로 구하는 방법
    ```
    // f(x)와 g(x)는 각각 오랜 시간이 걸리는 작업이라고 가정
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
    ```
    ```
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
      //Runnable 대신 Future API를 통해 코드를 더 단순화 하긴 했지만 submit 메서드 호출 같은 불필요한 코드가 있다.
      ExecutorService executorService = Executors.newFixedThreadPool(2);
      Future<Integer> y = executorService.submit(() -> f(x));
      Future<Integer> z = executorService.submit(() -> g(x));
      log.info("y : {}, z : {}, sum : {}", y.get(), z.get(), y.get() + z.get());
    }
    ```
    1. Future 형식 API
        - Future 형식 API 대안을 이용하면 f, g의 시그니처와 호출방법이 다음처럼 바뀐다.
        ```
        // 시그니처? 리턴만 바뀌고 시그니처는 안바꼈는데;;
        Future<Integer> f(int x);
        Future<Integer> g(int x);
        // 호출
        Future<Integer> y = f(x);
        Future<Integer> z = g(x);
        System.out.println(y.get() + z.get());
        ```
        - 메서드 f, g는 호출 즉시 자신의 원래 바디를 평가하는 태스크를 포함하는 Future를 반환한다.
        - 뭔지 잘 모르겠다 16장 실용예제에서 나온다니 그 때 다시봐야겠다.
    2. 리액티브 형식 API
        - f, g의 시그니처를 바꿔서 콜백 형식의 프로그래밍을 이용하는 것이 핵심
            - 이 책에서 콜백 : 메서드가 반환된 다음에 호출될 수 있는 람다나 메서드 참조를 가리키는 용어
        ```
        // 책에있던 코드
        void f(int x, IntConsumer dealWithResult);
        // 다운로드 받은 Functions 클래스에 있던 코드
        private void f(int x, IntConsumer dealWithResult) {
          log.info("reactive programming");
          dealWithResult.accept(Functions.f(x));
        }
        private void g(int x, IntConsumer dealWithResult) {
          log.info("reactive programming");
          dealWithResult.accept(Functions.g(x));
        }
        ```
        - 메서드 f에 추가 인수로 콜백(람다)을 전달해서 f의 바디에서는 return문으로 결과를 반환하는 것이 아니라
        - 결과가 준비되면 이를 람다로 호출하는 태스크를 만드는 것이 비결이다.
        - f는 바디를 실행하면서 태스크를 만든 다음 즉시 반환하므로 코드 형식이 아래처럼 바뀐다.
        ```
        @Test
        public void reactiveProgramming() {
          int x = 1337;
          Result result = new Result();
          f(x, (int y) -> {
            result.setLeft(y);
            log.info("sum : {}", result.getLeft() + result.getRight());
          });
          g(x, (int z) -> {
            result.setRight(z);
            log.info("sum : {}", result.getLeft() + result.getRight());
          });
        }
        ```
        ```
        // 출력 결과
        reactive programming
        sum : 2674
        reactive programming
        sum : 4012
        ```
        - 문제점 : f와 g의 호출 합계를 정확하게 출력하지 않고 상황에 따라 먼저 계산된 결과를 출력
            - 해결방법
                1. if-then-else를 이용해 적절한 락을 걸어 두 콜백이 모두 호출됐는지 확인한 다음 println호출
                2. Future를 이용하는 것이 더 적절 : 리액티브 형식의 API는 보통 한 결과가 아니라 일련의 이벤트에 반응하도록 설계되었기 때문
        - 리액티브 형식의 프로그래밍으로 메서드 f와 g는 dealWithResult콜백을 여러 번 호출할 수 있다.
            - 리액티브 형식의 비동기 API는 자연스럽게 일련의 값(나중에 스트림으로 연결)을 처리하는데 적합
            - Future 형식의 API는 일회성의 값을 처리하는 데 적합하다.
    - Future 형식, 리액티브 형식 모두 코드를 복잡하게 만드는 것 처럼 보일 수 있다.
        - 하지만 장점이 있기 때문에 잘 쓰면 좋다
            - 명시적으로 스레드를 처리하는 코드에 비해 사용 코드를 더 단순하게 만들어 준다.
            - 높은 수준의 구조를 유지할 수 있게 도와준다.
            - 애플리케이션 효율성을 크게 향상 시킬 수 있다.
    3. 잠자기(그리고 기타 블로킹 동작)는 해로운 것으로 간주.
        - 스레드는 잠들어도 여전히 시스템 자원을 점유한다.
        - 이상적으로는 절대 기다리는 일을 만들지 말거나 코드에서 예외를 일으키는 방법으로 처리할 수 있다.
        ```
        //예제 A
        work1();
        Thread.sleep(10000);
        work2();
        ```
        ```
        //예제 B
        public class ScheduledExecutorServiceExample {    
          public static void main(String[] args) {
              ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);    
              work1();
              scheduledExecutorService.schedule(ScheduledExecutorServiceExample::work2, 10, TimeUnit.SECONDS);    
              scheduledExecutorService.shutdown();
          }    
          public static void work1() { System.out.println("Hello from Work1!"); }    
          public static void work2() { System.out.println("Hello from Work2!"); }    
        }
        ```
        - 위 두 예제 모드 스레드 풀에서 실행된다고 가정
        - 예제 A의 실행 과정
            - 코드는 스레드 풀 큐에 추가되며 나중에 차례가 되면 실행된다.
            - 하지만 코드가 실행되면 워커 스레드를 점유한 상태에서 아무것도 하지 않고 10초를 잔다.
            - 그리고 10초가 지나 깨어나서 work2()를 실행한 다음 작업을 종료하고 워커 스레드를 해제한다.
        - 예제 B의 실행 과정
            - work1()을 실행하고 종료한다.
            - work2()가 10초 뒤에 실행될 수 있도록 큐에 추가한다.
            - 10초 뒤에 work2()가 실행된다.
            ```
            //결과
            Hello from Work1!
            1... 2... 3... 4... 5... 6... 7... 8... 9... 10... Hello from Work2!
            ```
        - 가능하다면 I/O작업에도 이 원칙을 적용하는 것이 좋다.
    4. 현실성 확인
        - 병렬 하드웨어를 최대한 활용할 수 있는 방법
            - 시스템을 수많은 소규모 동시 실행 태스크로 설계
            - 블록할 수 있는 모든 동작을 비동기 호출로 구현
        - 그러나 현식적으로는 안됨
    5. 비동기 API에서 예외는 어떻게 처리하는가?
        - 그러게요? 궁금하네
        - 비동기 API에서 호출된 메서드의 실제 바디는 별도의 스레드에서 호출된다.
            - 이 때 발생하는 어떤 에러는 이미 호출자의 실행 범위와는 관계 없는 상황이 된다.
            - 예상치 못한 일이 일어나면 예외를 발생시켜 다른 동작이 실행되어야 한다.
            - 어떻게??
            - Future를 구현한 CompletableFuture에서는 런타임 get() 메서드에 예외를 처리할 수 있는 기능을 제공하며, 예외에서 회복할 수 있도록 exceptionally()같은 메서드도 제공한다.
        - 리액티브 API에 여러 콜백을 포함
        ```
        void f(int x, Consumer<Integer> dealWithResult, Consumer<Throwable> dealWithException);
        ```
        - 콜백이 여러 개면 이를 따로 제공하는 것보다는 한 객체로 이 메서드를 감싸는 것이 좋다.
        ```
        void onCompelete()  //값을 다 소진했더나 에러가 발생해서 더 이상 처리할 데이터가 없을 때
        void onError(Throwable throwable)   //도중에 에러가 발생했을 떄
        void onNext(T item) //값이 있을 때
        ```
3. 박스와 채널 모델
    - 동시성 모델을 가장 잘 설계하고 개념화하기 위해 그림을 그리는 기법
    - 병렬성을 극대화하기 위해서는 모든 함수를 Future로 감싸는게 좋다.
        - 많은 태스크가 Future의 get()메서드를 호출하면 데드락에 걸릴 수 있다.
        - CompletableFuture와 Combinators를 이용해 문제를 해결할 수 있다.
4. CompletableFuture와 콤비네이터를 이용한 동시성
    - Future인터페이스의 문제 : 동시 코딩 작업을 Future인터페이스로 생각하도록 유도하는 점
    - CompletableFuture는 Future들을 조합하는 기능이다.
        - 일반적으로 Future는 실행해서 get()으로 결과를 얻을 수 있는 Callable로 만들어진다.
        - 그러나 CompletableFuture는 실행할 코드 없이 Future를 만들 수 있도록 허용한다.
        ```
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
        ```
        - CompletableFuture의 thenCombine활용
        - cfF와 cfG의 결과를 알지 못한 상태에서 thenCombine은 두 연산이 끝났을 때 스레드 풀에서 실행된 연산을 만든다.
        - 결과를 추가하는 세 번째 연산 cfCombine은 다른 두 작업이 끝날 때까지는 스레드에서 실행되지 않는다.
        ~~~
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
        ~~~
5. 발행-구독 그리고 리액티브 프로그래밍
    0. 리액티브 프로그래밍?
        - Future와 CompletableFuture는 독립적 실행과 병렬성이라는 정식적 모델 기반
        - 따라서 Future는 한 번만 실행해 결과를 제공한다.
        - 반면 리액티브 프로그래밍은 시간이 흐르면서 여러 Future같은 객체를 통해 여러 결과를 제공한다.
        - Java9에 추가된 발행-구독 모델을 적용한 Flow API
            1. 구독자가 있고 구독자가 구독할 수 있는 발행자가 있다.
            2. 이 연결을 구독(subscription)이라 한다.
            3. 이 연결을 이용해 메시지(또는 이벤트로 알려짐)를 전송한다.
    1. 두 Flow를 합치는 예제
        - 두 값 셀의 합을 보여주는 결과 셀을 정해두고 각 값 셀이 변화할 때 마다 결과 셀이 변화하는 기능
        ```
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
        ```
    2. 역압력
        - Publisher에서 Subcriber로 정보를 전달한다.
        - Java9에서 Flow API의 Subscriber 인터페이스는 void onSubscribe(Subcription subscription) 메서드를 포함한다.
            - Pub - Sub 간 채널이 연결되면 첫 번째로 호출되는 메서드
            - Subscription 객체는 Sub - Pub 간 통신할 수 있는 메서드를 포함한다.
            ```
            interface Subscription {
              void cancel();
              void request(long n);
            }
            ```
        - Publisher가 Subscription 객체를 만들어 Subscriber로 전달하면 Subscriber는 이를 이용해 Publisher로 정보를 보낼 수 있다.
    3. 실제 역압력의 간단한 형태
        - 한 번에 한 개의 이벤트를 처리하도록 발행-구독 연결을 구성하려면 아래와 같은 작업이 필요하다.
            1. Subscriber가 OnSubscribe로 전달된 Subscription 객체를 subscription같은 필드에 저장
            2. Subscriber가 수 많은 이벤트를 받지 않도록 onSubscribe, onNext, onError의 마지막 동작에 chnnel.request(1)을 추가해 오직 한 이벤트만 요청한다.
            3. 요청을 보낸 채널에만 onNext, onError이벤트를 보내도록 Publisher의 notifyAllSubscribers 코드를 바꾼다
        - 역압력을 구현할 때 고려해야 할 점들
            - 여러 Subscriber가 있을 때 이벤트를 가장 느린 속도로 보낼 것인가 아니면 각 Subscriber에게 보내지 않은 데이터를 저장할 별도의 큐를 가질 것인가?
            - 큐가 너무 커지면 어떻게 할까?
            - Subscriber가 준비가 안되었다면 큐의 데이터는 폐기할 것인가?
            - 데이터의 성격에 따라 다름. 온도 데이터 vs 은행 계좌 크레딧 데이터
6. 리액티브 시스템 vs 리액티브 프로그래밍
    - 리액티브 시스템
        - 런타임 환경이 변화에 대응하도록 전체 아키텍쳐가 설계된 프로그램을 지칭
        - 요악하자면 반응성, 회복성, 탄력성 세 가지 속성을 갖추도록 설계된 프로그램
            - 반응성 : 큰 작업을 처리하느라 간단한 질의의 응답을 지연하는 일 없이 실시간으로 입력에 반응하는 것
            - 회복성 : 한 컴포넌트의 실패가 전체 시스템의 실패로 연결되지 않음을 의미
            - 탄력성 : 자신의 작업 부하에 맞게 적응하며 작업을 효율적으로 처리함을 의미
    - 리액티브 프로그래밍
        - Java9 Flow 관련 자바 인터페이스에서 제공하는 형식
        - 리액티브 시스템을 구성하는 방법 중 하나
        - 메시지 주도(message-driven) 속성을 반영