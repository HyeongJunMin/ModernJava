# 목차

> 요약 및 결론  
> 책 내용  

---

## 요약 및 결론
- Flow API는 Akka, RxJava등의 리액티브 라이브러리를 이용해 개발된 리액티브 애플리케이션 간 공용어를 제시하는 역할을 한다.
- 
## 책 내용
> 리액티브 프로그래밍 패러다임의 중요성이 증가하는 이유
> 1. 빅테이터 : 페타 바이트 단위의 큰 데이터를 처리할 필요성이 생김
> 2. 다양한 환경 : 모바일 디바이스 부터 수천 개의 프로세서로 실행되는 클라우드 기반 클러스터 까지 다양한 환경에 애플리케이션이 배포된다
> 3. 사용 패턴 : 사용자는 1년 내내 항상 지속되는 서비스와 밀리초 단위의 응답 시간을 기대한다.
> 
> 리액티브 프로그래밍에서는 
> 다양한 시스템과 소스에서 들어오는 데이터 항목 스트림을 
> ***비동기적으로 처리하고 합쳐서*** 문제를 해결한다.

### 1. 리액티브 매니패스토(https://www.reactivemanifesto.org)
> 리액티브 애플리케이션과 시스템 개발의 핵심 원칙
```
1. 반응성(responsive)
    - 빠를 뿐 만 아니라 일정하고 예상할 수 있는 반응 시간을 제공
    - 일관된 서비스 품질을 제공하는 데 중점
    - 회복성 때문에 지속되는 서비스를 제공할 수 있고, 탄력성 때문에 반응시간을 일정하게 유지할 수 있다.
2. 회복성(resilient)
    - 장애가 발생해도 시스템은 반응하고 장애를 복구한다.
    - 컴포넌트 실행 복제, 여러 컴포넌트의 시간과 공간 분리, 각 컴포넌트에 위임하는 등 다양한 기법 존재
3. 탄력성(elastic)
    - 무거운 작업 부하가 발생하면 자동으로 관련 컴포넌트에 할당된 리소스를 늘려서 대응한다.
4. 메시지 주도(message-driven)
    - 회복성과 탄력성을 지원하려면 컴포넌트 간 경계를 명확하게 정의해야 한다.
    - 비동기 메시지를 전달해 컴포넌트 끼리의 통신이 이루어지도록 한다.
    - 장애를 메시지로 처리해서 회복성을 얻을 수 있다.
    - 주고 받은 메시지 수를 감시하고 메시지 양에 따라 적절하게 리소스를 할당할 수 있다.
```
1. 애플리케이션 수준의 리액티브
    - 주요 기능은 비동기로 작업을 수행할 수 있다는 점
    - CPU 사용률을 극대화 하기 위해 스레드를 퓨처, 액터, 이벤트 루프 등과 공유하고 처리할 이벤트를 변환하고 관리한다.
2. 시스템 수준의 리액티브
    - 리액티브 시스템은 여러 애플리케이션이 한 개의 일관적이고 회복할 수 있는 플랫폼을 구성하게 해준다. 뿐 만 아니라 이들 애플리케이션 중 하나가 실패해도 전체 시스템은 계속 운영될 수 있도록 도와주는 소프트웨어 아키텍쳐다.
        `리액티브 원칙 적용은 각 애플리케이션이 될 수도 시스템이 될 수도 있다는 이야기 인가 보다.`
    - 리액티브 시스템의 주요 속성으로 메시지 주도를 꼽을 수 있다.
    - 각 컴포넌트에서 발생한 장애를 고립시켜서 회복성을 제공한다. ***회복성의 핵심은 고립과 비결합이다.***
    - 탄력성의 핵심은 위치 투명성이다
        `리액티브 시스템의 모든 컴포넌트가 수신자의 위치에 상관없이 다른 모든 서비스와 통신할 수 있음을 의미`
        `덕분에 시스템을 복제하며 작업 부하에 따라 애플리케이션을 확장할 수 있다.`
        

### 2. 리액티브 스트림과 플로 API
> 리액티브 프로그래밍은 리액티브 스트림을 사용하는 프로그래밍이다.
```
1. 리액티브 스트림은 잠재적으로 무한의 비동기 데이터를 순서대로, 그리고 블록하지 않는 역압력을 전제해 처리하는 표준 기술이다.
    - 역압력? : 발행-구독 프로토콜에서 발행자가 이벤트를 제공하는 속도보다 구독자의 이벤트 소비 속도가 느릴 때 문제가 발생하지 않도록 보장하는 장치.
    - 역압력 예시 : 알림의 대상은 업스트림 발행자
        1. 부하가 발생한 컴포넌트는 이벤트 발생 속도를 늦추라고 알림
        2. 얼마나 많은 이벤트를 수신할 수 있는지 알림
        3. 다른 데이터를 받기 전에 기존 처리 속도가 얼마나 걸리는지 알림
2. 리액티브 스트림 구현이 제공해야 하는 최소 기능 집합을 정의한 API
    @FunctionalInterface
    public static interface Publisher<T> {
      public void subscribe(Subscriber<?superT> subscriber);
    }
    public static interface Subscriber<T> {
      public void onSubscribe(Subscription subscription);
      public void onNext(Titem);
      public void onError(Throwable throwable);
      public void onComplete();
    }
    public static interface Subscription {
      public void request(long n);
      public void cancel();
    }
    public static interface Processor<T,R> extends Subscriber<T>, Publisher<R> { }
```
1. Flow 클래스 소개
    - Java9에 추가된 리액티브 프로그래밍을 제공하는 클래스
    - 네 개의 중첩된(nested) 인터페이스 포함(이너클래스 처럼)
        - Publisher : 항목을 발행
        - Subscriber : 항목을 소비, 콜백 메서드 네 개를 정의
        ```
        // onSubscribe는 항상 처음 호출되어야 한다.
        // onNext는 onSubscribe 다음에 호출되어야 하며 여러 번 호출될 수 있다.
        // onNext 다음에 onError 또는 onComplete가 호출되어야 한다.
        onSubscribe onNext* (onError | onComplete)
        ```
        - Subscription : 발행과 소비 과정을 관리(역압력 포함)
        - Processor : 이벤트의 단계를 나타냄
    - 인터페이스 간 규칙 요약
        ```
        1. Publisher는 반드시 Subscription의 request 메서드에 정의된 개수 이하의 요소만 Subscriber에 전달해야 한다.
            - 지정된 개수보다 적은 수의 요소를 onNext로 전달할 수 있다.
            - onComlete, onError를 호출해 Subscription을 종료할 수 있다.
        2. Subscriber는 요소를 받아 처리할 수 있음을 Publisher에게 알려야 한다.
            - Publisher에게 역압력을 행사하고 관리한계를 넘지 않도록 제어할 수 있다.
            - onComplete, onError신호를 처리하는 상황에서는 Subscription이 취소됐다고 가정한다. 
             > Publisher나 Subscription의 어떤 메서드도 호출할 수 없다.
            - Subscription의 시그널을 받을 준비가 되어있어야 한다.
             > Subscribtion.request() 메서드 호출이 없어도 언제든 종료 시그널을 받을 준비가 돼있어야 한다.
             > Subscription.cancel()이 호출된 이후라도 한 개 이상의 onNext를 받을 준비가 돼있어야 한다.
        3. Publisher와 Subscriber는 정확하게 Subscription을 공유해야 하며 각각이 고유한 역할을 수행해야 한다.
            - Subscriber는 onSubscribe와 onNext메서드에서 request메서드를 동기적으로 호출할 수 있어야 한다.
            - Subscription.cancel() 메서드는 몇 번을 호출해도 한 번 호출한 것과 같은 효과를 가져야 한다.
        ```
2. 첫 번째 리액티브 애플리케이션 만들기
    - 리액티브 원칙이 적용된 온도 보고 프로그램
        - TempInfo : 원격 온도계를 흉내낸다(0에서 99 사이의 화씨 온도를 랜덤으로 만들어 연속적으로 보고)
        - TempSubscription : 온도를 전송한다.
        - TempSubscriber : 각 도시에 설치된 센서에서 보고한 온도 스트림을 출력
    - 예제 코드
        ```java
        // 현재 보고된 온도를 전달하는 자바 빈
        @Getter
        @AllArgsConstructor
        @ToString
        public class TempInfo {
          public static final Random random = new Random();
          private final String town;
          private final int temp;
          public static TempInfo fetch(String town) {
            if (random.nextInt(10) == 0) {
              throw new RuntimeException("Temperature Fetch Error!"); //10% 확률로 작업 실패
            }
            return new TempInfo(town, random.nextInt(100));
          }
        }
        ```
        ```java
        // Subscriber에게 TempInfo 스트림을 전송하는 Subscription
        @AllArgsConstructor
        public class TempSubscription implements Subscription {
          private final Subscriber<? super TempInfo> subscriber;
          private final String town;
          @Override
          public void request(long n) {
            for (long i = 0L; i < n; i++) { //Subscriber가 만든 요청을 한 개씩 반복
              try {
                subscriber.onNext(TempInfo.fetch(town));
              } catch (Exception e) {
                subscriber.onError(e);  //온도 가져오기 실패하면 Subscriber로 에러 전달
                break;
              }
            }
          }
          @Override
          public void cancel() {
            subscriber.onComplete();  //구독 취소되면 완료신호를 Subscriber로 전달  
          }
        }
        ```
        ```java
        // 레포트를 요청하고 받은 온도를 출력하는 Subscriber
        @Slf4j
        public class TempSubscriber implements Subscriber<TempInfo> {
          private Subscription subscription;
          @Override
          public void onSubscribe(Subscription subscription) {
            // 구독을 저장하고 첫 번째 요청을 전달
            this.subscription = subscription;
            subscription.request(1L);
          }
          @Override
          public void onNext(TempInfo tempInfo) {
            // 수신한 온도를 출력하고 다음 정보를 요청
            log.info("temperature info : {}", tempInfo);
            subscription.request(1L);
          }
          @Override
          public void onError(Throwable throwable) {
            log.error("temperature fetch error : {}", throwable.getMessage());
          }
          @Override
          public void onComplete() {
            log.info("done!");
          }
        }
        ```
        ```java
        public class Ch17ReactiveProgramming {
            @Test
            void subscribe() {
              // TempSubscriber를 이용해 Publisher에 구독하도록 구현      
              getTemperatures("New York").subscribe(new TempSubscriber());
              // temperature info : TempInfo(town=New York, temp=81)
              // temperature info : TempInfo(town=New York, temp=24)
              // temperature info : TempInfo(town=New York, temp=19)
              // temperature fetch error : Temperature Fetch Error!
            }
            // 리액티브 애플리케이션이 동작할 수 있도록 만든 Publisher      
            private static Publisher<TempInfo> getTemperatures(String town) {
              return subscriber -> subscriber.onSubscribe(
                  new TempSubscription(subscriber, town)
              );
            }
        }
        ```
### 3. 리액티브 라이브러리 RxJava 사용하기
### 4. 마치며