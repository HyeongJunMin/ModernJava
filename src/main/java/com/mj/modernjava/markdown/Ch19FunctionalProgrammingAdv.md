# 목차

> 요약 및 결론  
> 책 내용  

---

## 요약 및 결론
- 함수형 프로그래밍이 무엇인지 왜쓰는지 어떻게쓰는지 알려줌
- 핵심 용어
    - 고차원 함수, 참조 투명성, 영속 자료구조
- 커링이랑 고차원함수 부분은 언젠가는 쓸 수도 있을듯 함

## 책 내용
> 실용적 함수형 프로그래밍 기법과 학술적 지식을 소개한다.
```
고차원 함수, 커링, 영구 자료구조, 게으른 리스트, 패턴 매칭, 참조 투명성을 이용한 캐싱과 콤비네이터 
```
### 1. 함수는 모든 곳에 존재한다.
> 자바8은 일급 함수를 지원한다.(함수를 일반값 처럼 사용해서 인수로 전달하거나 결과로 반환받거나 자료구조에 저장할 수 있음)
> 예시 : Function<String, Integer> strToInt = Integer::parseInt;
1. 고차원 함수
    - 하나 이상의 동작을 수행하는 함수
        - 하나 이상의 함수를 인수로 받음
        - 함수를 결과로 반환
    - 자바8의 함수도 고차원 함수라고 할 수 있다.
        - 인수로 전달, 결과로 반환, 지역 변수로 할당, 구조체로 삽입이 모두 가능하기 때문
2. 커링
    - 정의 : x, y 두 인수를 받는 함수 f를 하나의 인수를 받는 g로 대체하는 기법
        - f(x, y) = (g(x))(y)
    - 예제
        - 섭씨 x, 변환요소 f, 기준치 조정요소 b가 있다.
        - 재사용성이 높은 코드를 만든다.
        ````java
        @Slf4j
        public class Ch19FunctionalProgrammingAdv {
          
          public class ConversionFactors {
            public static final double KM_TO_MILE = 0.6214;
            public static final double CELSIUS_TO_FAHRENHEIT = 0.6214;
          }
        
          public class BaselineAdjustmentFactors {
            public static final double CELSIUS_TO_FAHRENHEIT = 32;
          }
        
          @Test
          public void curring() {
            double kiloMeter = 100;
            DoubleUnaryOperator convertKmToMi = curriedConverter(ConversionFactors.KM_TO_MILE, 0);
            double mile = convertKmToMi.applyAsDouble(kiloMeter);
            log.info("kilo meter to mile : kilo meter = {}, mile = {}", kiloMeter, mile);
            // kilo meter to mile : kilo meter = 100.0, mile = 62.13999999999999
        
            double celsius = 25;
            DoubleUnaryOperator convertCToF = curriedConverter(ConversionFactors.CELSIUS_TO_FAHRENHEIT, BaselineAdjustmentFactors.CELSIUS_TO_FAHRENHEIT);
            double fahrenheit = convertCToF.applyAsDouble(celsius);
            log.info("celsius to fahrenheit : celsius = {}, fahrenheit = {}", celsius, fahrenheit);
            // celsius to fahrenheit : celsius = 25.0, fahrenheit = 47.535
          }
          
          public static DoubleUnaryOperator curriedConverter(double f, double b) {
            return (double x) -> x * f + b;
          }
        
        }
        ````

### 2. 영속 자료구조
> 함수형 프로그래밍에서 사용하는 자료구조 = 영속 자료구조
> 여기서 영속(persist)의 의미는 저장된 값이 다른 누군가에  의해 영향을 받지 않는 상태를 의미
```
1. 함수는 0개 이상의 인수를 가지며 한 개 이상의 결과를 반환하지만 부작용이 없어야 한다.
2. 함수는 부작용이 없다 == 함수는 다른 객체의 상태를 바꾸지 않는다. == 함수는 의도한 계산 외에는 시스템에 미치는 영향이 전혀 없다.
```
1. 파괴적인 갱신과 함수형
    - 파괴적인 갱신 : 기존에 있던 값을 바꾸며 갱신 -> 부작용 초래
    - 함수형 갱신 : 기존에 있던 값은 그대로 두고 그 값을 복사한 값을 결과에 포함시킴
    - 예제 내용자체는 이해가 안되지만 말하려는게 뭔지는 알겠다.
        - 계산결과를 표현할 자료구조가 필요하면?
        - 기존의 자료구조에 영향이 없는 새로운 자료구조를 만들어서 보여줘야 한다.
        - 부작용을 방지하기 위함
    - 아 예제 이해함
        - seoulToGumi가 있는 상황에서
        - gumiToBusan을 붙인다고 하면?
            - seoulToGumi는 더이상 seoulToGumi가 아닌 seoulToBusan이 될것
            - 그러니까 파괴적이라는거고
        - 함수형 갱신이 되려면
            - seoulToGumi는 그대로 두고
            - gumiToBusan에 새로만든 seoulToGumi2를 엮어준다고
    - ㅇㅋ
    ```java
    @AllArgsConstructor
    @Getter @Setter
    @ToString(exclude = "onWard")
    public class TrainJourney {
      private String name;
      private int price;
      private TrainJourney onWard;
      public TrainJourney (String name) {
        this.name = name;
      }
      public TrainJourney (int price, TrainJourney onWard) {
        this.price = price;
        this.onWard = onWard;
      }
      // 부작용이 발생하는 파괴적 갱신
      public static TrainJourney link(TrainJourney targetJourney, TrainJourney newJourney) {
        if (targetJourney == null) {
          return newJourney;
        }
        TrainJourney t = targetJourney;
        while(t.getOnWard() != null) {
          t = t.getOnWard();
        }
        t.setOnWard(newJourney);
        return targetJourney;
      }
      //부작용을 없앤 함수형 갱신
      public static TrainJourney append(TrainJourney targetJourney, TrainJourney newJourney) {
        return targetJourney == null ? newJourney : new TrainJourney(targetJourney.getPrice(), append(targetJourney.getOnWard(), newJourney));
      }
    }
    @Slf4j
    public class Ch19FunctionalProgrammingAdv {
      @Test
      public void destructiveAndFunctionalUpdate() {
        TrainJourney busan = new TrainJourney("Busan");
        TrainJourney gumi = new TrainJourney("Gumi");
        TrainJourney seoul = new TrainJourney("Seoul", 5000, gumi);
        TrainJourney link = TrainJourney.link(seoul, busan);
        log.info("result :{}", link);
        log.info("result :{}", link.getOnWard());
        log.info("result :{}", link.getOnWard().getOnWard());
        // result :TrainJourney(name=Seoul, price=5000)
        // result :TrainJourney(name=Gumi, price=0)
        // result :TrainJourney(name=Busan, price=0)
        TrainJourney mokpo = new TrainJourney("Mokpo");
        TrainJourney gwangju = new TrainJourney("Gwangju");
        TrainJourney jeonju = new TrainJourney("Jeonju", 5000, gwangju);
        TrainJourney append = TrainJourney.append(jeonju, mokpo);
        log.info("result :{}", append);
        log.info("result :{}", append.getOnWard());
        log.info("result :{}", append.getOnWard().getOnWard());
        // result :TrainJourney(name=Jeonju, price=5000)
        // result :TrainJourney(name=Gwangju, price=0)
        // result :TrainJourney(name=Mokpo, price=0)
      }
    }
    ```
2. 트리를 사용한 다른 예제
    - 트리 자료구조 예시를 보여주고 다음 함수형으로 접근
    ```java
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter @Setter
    @ToString
    public class Tree {
      private String key;
      private int value;
      private Tree left, right;
      public static Tree update(String key, int newValue, Tree tree) {
        if (tree == null) {
          tree = new Tree(key, newValue, null, null);
        } else if (key.equals(tree.getKey())) {
          tree.setValue(newValue);
        } else if (key.compareTo(tree.getKey()) < 0) {
          tree.setLeft(update(key, newValue, tree));
        } else {
          tree.setRight(update(key, newValue, tree));
        }
        return tree;
      }
    }
    public class TreeProcessor {
      public static int lookup(String key, int defaultValue, Tree tree) {
        if (tree == null) return defaultValue;
        if (key.equals(tree.getKey())) return tree.getValue();
        return lookup(key, defaultValue, key.compareTo(tree.getKey()) < 0 ? tree.getLeft() : tree.getRight());
      }
    }
    ```
3. 함수형 접근법 사용
    - 위 Tree update예제를 함수형으로 접근하려면?
    - 갱신을 수행할때마다 논리적으로 새로운 자료구조를 만든 다음, 사용자에게 적절한 버전의 자료구조를 전달해야 한다.
        - 기존 자료구조에는 변화가 일어나지 않는 것이 핵심
    - 그럼 매 번 Tree를 새로만드는데 유의미한 자료구조인가?
        - 여러 자료구조의 버전을 모두 저장했다가 적절한 버전을 사용하는 방식을 취하면 유의미하다고 한다.
    - 예제코드
    ```java
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter @Setter
    @ToString
    public class Tree {
      private String key;
      private int value;
      private Tree left, right;
      public static Tree fUpdate(String key, int newValue, Tree tree) {
        return (tree == null)
            ? new Tree(key, newValue, null, null)
            : key.equals(tree.getKey())
              ? new Tree(key, newValue, tree.getLeft(), tree.getRight()) 
              : key.compareTo(tree.getKey()) < 0
                ? new Tree(tree.getKey(), tree.getValue(), fUpdate(key, newValue, tree.getLeft()), tree.getRight())
                : new Tree(tree.getKey(), tree.getValue(), tree.getLeft(), fUpdate(key, newValue, tree.getRight()));
      }
    }
    ```
    
### 3. 스트림과 게으른 평가
> 스트림은 단 한번만 소비 가능하다는 제약이있기 때문에 재귀적으로 정의할 수 없다.
> 이 제약 때문에 어떤 문제가 발생하는지 살펴본다.
- 미리 정의하기에는 너무 큰 자료구조는 게으른 자료구조로 만들 수 있다.
    - 준비만 해 두고 요청이 있을 때만 자료구조 생성
1. 자기 정의 스트림
    - 소수 스트림 예제 코드
    - 후보 수(candidate number)로 정확히 나누어 떨어지는지 매번 모든 수를 반복 확인
    ```java
    @Slf4j
    public class Ch19FunctionalProgrammingAdv {
      @Test
      public void primesTest() {
        int number = 10;
        primes(10).forEach(result -> {
          log.info("result :{}", result);
        });
      }
      public static Stream<Integer> primes(int number) {
        return Stream.iterate(2, i -> i + 1)
            .filter(MathUtils::isPrime)
            .limit(number);
      }
      public static class MathUtils {
        public static boolean isPrime(int candidate) {
          int candidateRoot = (int) Math.sqrt((double) candidate);
          return IntStream.rangeClosed(2, candidateRoot)
              .noneMatch(i -> candidate % i == 0);
        }
      }
    }
    ```
    - 다른 방법으로 해보자
        - 알고리즘 설명 : 소수로 나눌 수 있는 모든 수는 제외할 수 있다.
        - 에라토스테네스의 체
            1. 소수를 선택할 숫자 스트림 필요
            2. 스트림에서 첫 번째 수를 가져온다. 이 숫자는 소수다(초기값 2)
            3. 스트림의 꼬리에서 가져온 수로 나누어 떨어지는 모든 수를 걸러 제외시킨다.
            4. 이런식으로 새로운 스트림에서 소수를 찾는 과정을 반복한다. 따라서 재귀 알고리즘이다.
        - 예제 코드
        ```java
        @Slf4j
        public class Ch19FunctionalProgrammingAdv {
          @Test
          public void primesByEratosthenesTest() {
            // 1단계 : 스트림 숫자 얻기(무한 숫자 스트림)
            IntStream numbers = IntStream.iterate(2, n -> n + 1);
            // 2단계 : 머리 획득(첫 번째 요소)
            int head = numbers.findFirst().getAsInt();
            // 3단계 : 꼬리 필터링
            IntStream filtered = numbers.skip(1).filter(n -> n % head != 0);
            // 4단계 : 재귀적으로 소수 스트림 생성
            primesByEratosThenes(numbers).limit(10).forEach(number -> log.info("number :{}", number));
          }
          private static IntStream primesByEratosThenes(IntStream numbers) {
            int head = numbers.findFirst().getAsInt();  // 스트림이 소비됨
            return IntStream.concat(
                      IntStream.of(head),
                      // 위에서 이미 소비됐기 때문에 스트림 사용 불가
                      primesByEratosThenes(numbers.skip(1).filter(n -> n % head != 0))
                    );
          }
        }
        ```
2. 게으른 리스트 만들기
    - 자바8의 스트림은 게으르다 : 요청할 때만 값을 생성해준다.
    - 요청할 때란? 최종연산
    - 게으른 리스트
        - 스트림과 비슷한 개념으로 구성됨
        - 고차원 함수라는 개념도 지원
        - 함숫값을 자료구조에 저장해서 함숫값을 사용하지 않은 상태로 보관할 수 있다.
        - 기본적인 게으른 리스트 예시
            - 꼬리가 모두 메모리에 존재하지 않게 할 수 있다.(Supplier활용)
            ```java
            @Slf4j
            public class Ch19FunctionalProgrammingAdv {
              @Test
              public void lazyListTest() {
                // 10부터 시작하는 무한 스트림
                LazyList<Integer> numbers = LazyList.from(10);
                log.info("1 :{}", numbers.head());
                log.info("2 :{}", numbers.tail().head());
                log.info("3 :{}", numbers.tail().tail().head());
                // 1 :10
                // 2 :11
                // 3 :12
                // 2부터 소수 찾기
                LazyList<Integer> from = LazyList.from(2);
                log.info("1 :{}", LazyList.primes(from).head());
                log.info("2 :{}", LazyList.primes(from).tail().head());
                log.info("3 :{}", LazyList.primes(from).tail().tail().head());
                // 1 :2
                // 2 :3
                // 3 :5
              }
            }
            public interface MyList<T> {
              T head();
              MyList<T> tail();
              MyList<T> filter(Predicate<T> p);
              default boolean isEmpty() {
                return true;
              }
            }
            public class LazyList<T> implements MyList<T>{
              private final T head;
              private final Supplier<MyList<T>> tail;
              public LazyList(T head, Supplier<MyList<T>> tail) {
                this.head = head;
                this.tail = tail;
              }
              public T head() {
                return head;
              }
              public MyList<T> tail() {
                return tail.get();
              }
              public boolean isEmpty() {
                return false;
              }
              public MyList<T> filter(Predicate<T> p) {
                return isEmpty()
                        ? this
                        : p.test(head())
                            ? new LazyList(head(), () -> tail().filter(p))
                            : tail().filter(p);
              }
              public static LazyList<Integer> from(int n) {
                return new LazyList<Integer>(n, () -> from(n + 1));
              }
              public static LazyList<Integer> primes(MyList<Integer> numbers) {
                return new LazyList<>(
                    numbers.head(),
                    () -> primes(
                        numbers.tail()
                              .filter(n -> n % numbers.head() != 0)
                    )
                );
              }
            }
            ```
    - 이득은 무엇인가?
        - 자료구조에 값 뿐만 아니라 함수를 저장할 수 있다.
        - 자료구조를 만드는 시점이 아니라 요청 시점에 실행할 수 있다.
        - 명확히 성능이 어떠한지는 파악하지 않았지만 좋을 것으로 예상 된다.
        - 상황에 맞게 사용해야 한다.
### 4. 패턴 매칭
> 함수형 프로그래밍을 구분하는 중요한 특징
> 거의 모든 함수형 프로그래밍 언어에서는 제공하지만, 자바에서는 지원하지 않는 기능
> 자바의 switch-case와 유사
1. 방문자 디자인 패턴
    - 방문자 디자인 패턴으로 자료형을 언랩할 수 있다.
    - 지정된 데이터 형식의 인스턴스를 인풋으로 받아 인스턴스의 모든 멤버에 접근한다.
    - 자바로 되는데?
    - 데이터 구조와 연산을 분리
        - 데이터 구조를 변경하지 않으면서 새로운 연산이 쉬움
        - 만약 새로운 연산(Directory, File외 무언가)을 추가하려면 새로운 visit() 메서드를 구현
    ```java
    @Slf4j
    public class Ch19FunctionalProgrammingAdv {
      @Test
      public void visitorPattern() {
        // Directory와 File은 Entry를 상속받고 있다.
        // Directory는 List<Entry> directory를 갖고있으며 add(Entry entry)메서드는 그 리스트에 Entry를 추가한다.
        // Directory.accept(Visitor visitor)를 호출하게되면
        //  > directory가 갖는 directory리스트의 모든 요소에 대해 Visitor가 갖는 visit을 수행(로그를 남김)
        //  > ViewVisitor는 오버로딩된 visit메서드 2개가 있다 visit(Directory directory), visit(File file)
        // 결론적으로 accept가 호출된 객체가 갖는 모든 Entry들과 그 하위 Entry들에 대해 visit을 수행하게 된다.
        Directory root = new Directory("root");
        Directory bin = new Directory("bin");
        Directory share = new Directory("share");
        File conf = new File("conf");
        File readme = new File("readme");
        File img = new File("img");
        File img2 = new File("img2");
        root.add(conf);
        bin.add(readme);
        root.add(bin);
        share.add(img);
        share.add(img2);
        root.add(share);
        root.accept(new ViewVisitor());
        // /root
        // /root/conf
        // /root/bin
        // /root/bin/readme
        // /root/bin/share
        // /root/bin/share/img
        // /root/bin/share/img2
      }
    }
    ```
2. 패턴 매칭의 힘
    - 쉬운 방법의 switch문
    - 자바8 람다로 비슷한 코드 만들기 가능... 진짜 복잡
    ```java
    public class PatternMatching {
      public static Expr simplify(Expr e) {
        TriFunction<String, Expr, Expr, Expr> binOperCase =
            (operator, left, right) -> {
              if ("+".equals(operator)) {
                if (left instanceof Number && ((Number)left).val == 0) {
                  return right;
                }
                if (right instanceof Number && ((Number)right).val == 0) {
                  return right;
                }
              }
              if ("*".equals(operator)) {
                if (left instanceof Number && ((Number)left).val == 0) {
                  return right;
                }
                if (right instanceof Number && ((Number)right).val == 0) {
                  return right;
                }
              }
              return new BinOp(operator, left, right);
            };
        Function<Integer, Expr> numCase = val -> new Number(val); // 숫자 처리
        Supplier defaultCase = () -> new Number(0); // 수식을 인식할 수 없을 때 기본처리
        return patternMatchExpr(e, binOperCase, numCase, defaultCase);
      }
    }
    ```

### 5. 기타 정보
> 함수형, 참조 투명성
1. 캐싱 또는 기억화
    - 기억화는 메서드에 래퍼로 캐시를 추가하는 기법
    - 래퍼가 호출되면 캐시에 존재하는지 먼저 확인하고 있으면 리턴, 없으면 계산
    - 다수의 호출자가 공유하는 자료구조를 갱신하기 때문에 순수 함수형 해결방식은 아니다
    - 그러나 감싼 버전의 코드는 참조 투명성을 유지할 수 있다.
    ```java
    @Slf4j
    public class Ch19FunctionalProgrammingAdv {
      @Test
      public void cachingMemorization() {
        Integer result1 = computeNumberOfNodesUsingCache("1");
        log.info("result 1 :{}", result1);
        Integer result2 = computeNumberOfNodesUsingCache("1");
        log.info("result 2 :{}", result2);
      }
      private static final Map<String, Integer> numberOfNodes = new HashMap();
      Integer computeNumberOfNodesUsingCache(String id) {
        Integer result = numberOfNodes.get(id);
        if (result != null) {
          return result;
        }
        result = compute();
        numberOfNodes.put(id, result);
        return result;
      }
      private static Integer compute() {
        try {
          Thread.sleep(1000L);
        } catch (Exception e) {
    
        }
        return 10;
      }
    }
    ```
2. '같은 객체를 반환함'은 무엇을 의미하는가?
    - 참조 투명성이란 ‘인수가 같다면 결과도 같아야 한다’는 규칙을 만족함을 의미한다.
    - 트리예제에서 fupdate는 서로 다른 참조이지만 구조적인 값이 같으므로 fupdate는 참조 투명성을 갖는다고 이야기할 수 있다.
3. 콤비네이터
    - 함수형 프로그래밍에서는 두 함수를 인수로 받아 다른 함수를 반환하는 등 함수를 조합하는 고차원 함수를 많이 사용하게 된다.
    - 함수를 조합하는 기능을 콤비네이터라고 한다.
    - 함수 조합 예시
    ```java
    @Slf4j
    public class Ch19FunctionalProgrammingAdv {
      @Test
      public void combineFunctionsTest() {
        Integer result = Combinators
            // x -> (2 * (2 * (2 * x) ) ) 또는 x -> 8*x
            .repeat(3, (Integer x) -> 2 * x)
            .apply(10);
        log.info("result :{}", result);
      }
    }
    public class Combinators {
      // 함수 f와 g를 인수로 받아 f의 기능을 적용한 다음 g의 기능을 적용하는 함수를 반환
      public static <A, B, C> Function<A, C> compose(Function<B, C> g, Function<A, B> f) {
        return x -> g.apply(f.apply(x));
      }
      // f에 연속적으로 n번 적용
      public static <A> Function<A, A> repeat(int n, Function<A, A> f) {
        return n == 0 ? x -> x : compose(f, repeat(n - 1, f));
      }
    }
    ```
### 6. 마치며
- 일급 함수란 인수로 전달하거나, 결과로 반환하거나, 자료구조에 저장할 수 있는 함수다.(Function<T>)
- 고차원 함수란 한 개 이상의 함수를 인수로 받아서 다른 함수를 반환하는 함수이다.
- 커링은 함수를 모듈화하고 코드를 재사용할 수 있도록 지원하는 기법이다
    - 고차원 함수를 만드는 기법이다?
- 영속 자료구조는 갱신될 때 기존버전의 자신을 보존하므로 별도의 복사과정이 필요하지 않다
- 게으른 리스트는 자바 스트림보다 비쌈
- 패턴 매칭은 자료형을 언랩하는 함수형 기능이다. 자바의 switch문을 일반화 할 수 있다.
- 참조 투명성을 유지하는 상황에서는 계산 결과를 캐시할 수 있다.
    - 참조 투명성 : ‘인수가 같다면 결과도 같아야 한다’
- 콤비네이터는 둘 이상의 함수나 자료구조를 조합하는 함수형 개념이다.