# 목차

> 요약 및 결론  
> 책 내용  

---

## 요약 및 결론
- 함수형 프로그래밍이 무엇인지 왜쓰는지 어떻게쓰는지 알려줌
- 심화는 다음시간에
- 함수형 프로그래밍? 인수가 일정할 때 항상 동일한 결과를 호출하며 시스템의 다른 부분에 영향을 주지 않도록 프로그래밍 하는 방법
- 함수형 프로그래밍 왜씀? 부작용 없음과 불변성이 확보되어 유지보수성에 도움이 됨, 가독성도 좋음
- 어떻게 씀? 예제 확인
- 계산을 도와주기 위해 common하게 만든 Util클래스에 적용하기 좋아보인다

## 책 내용
> 왜 함수형 프로그래밍을 사용하는가?
```
1. 함수형 프로그래밍의 개념은 무엇인가?
2. 함수형 프로그래밍을 적용하면 무엇이 좋을까? 
```
### 1. 시스템 구현과 유지보수
> 함수형 프로그래밍이 제공하는 부작용 없음과 불변성 개념은 유지보수성에 도움이 된다.
> .
> 요약 : 공유된 가변 데이터는 사이드 이펙트를 발생시킬 확률이 매우 높아서 유지보수가 힘들다. 선언형 프로그램을 기반으로 한 함수형 프로그래밍을 활용하면 부작용 없이 유지보수성 좋은 코드를 만들 수 있다. 
1. 공유된 가변 데이터
    - 변수가 예상하지 못한 값을 가지게 되는 이유? : 공유된 가변 데이터를 읽고 갱신하기 때문
    - 하나의 리스트를 참조하는 여러 클래스가 있는 경우에 하나의 클래스가 리스트를 갱신하면?
        - 다른 클래스들은 갱신된 사실을 알고 있을까? 아니라면 어떻게 알려줘야 될까?
        - 프로그램 전체에서 데이터 갱신 사실을 추적하기 어려워진다.
    - 그럼 어떻게 바꾸면 좋지???
        - 부작용 없는(side-effect free)메서드 
            - 자신을 포함하는 클래스의 상태나 다른 객체의 상태를 바꾸지 않으며 return 문을 통해서만 자신의 결과를 반환하는 메서드
            - 예상치 못한 자료구조의 변경이 일어날 수 없다면 유지보수 하기 쉬움
        - 불변 객체
            - 인스턴스화 된 다음에는 객체의 상태를 바꿀 수 없는 객체 == 함수 동작에 영향을 받지 않는다.
            - 즉 예상치 못한 상태로 바뀌지 않는다.
2. 선언형 프로그래밍
    - 함수형 프로그래밍의 기반을 이루는 개념
    - 시스템을 구현하는 방식은 크게 명령형과 선언형으로 나눌 수 있음
        - 명령형 : 이거 먼저 하고 그 다음 저거 하고 그 다음...
        - 선언형 : 내부반복 처럼 구현방법을 라이브러리가 결정하는 느낌
3. 왜 함수형 프로그래밍인가?
    - 함수형 프로그래밍은 선언형 프로그래밍을 따르는 대표적인 방식이다.
    - 부작용 없는 계산을 지향한다.
    - 한 눈에 쉽게 볼 수 있도록 코드를 작성할 수 있기 때문에 가독성이 좋아진다.(람다)

### 2. 함수형 프로그래밍이란 무엇인가?
> 먼저, 함수형 프로그래밍에서 함수는 무엇인가?
```
1. 함수는 0개 이상의 인수를 가지며 한 개 이상의 결과를 반환하지만 부작용이 없어야 한다.
2. 함수는 부작용이 없다 == 함수는 다른 객체의 상태를 바꾸지 않는다. == 함수는 의도한 계산 외에는 시스템에 미치는 영향이 전혀 없다.
```
1. 함수형과 자바
    - 실제로는 부작용이 있지만 아무도 보지 못하게 해서 함수형을 달성한다.
    - ***함수나 메서드***가 함수형 프로그래밍이기 위한 조건들
        - 지역 변수만을 변경한다.
        - 참조하는 객체가 있다면 그 객체는 불변객체여야 한다.
        - 어떤 예외로 일으키지 않아야 한다. -> return으로 결과를 반환할 수 없기 때문
        - 부작용을 포함하는 라이브러리 함수를 사용하는 경우는 오직 비함수형 동작을 감출 수 있는 상황에서만 사용한다.
2. 참조 투명성
    - 어떤 함수를 몇 번 호출하든지 인수가 동일하다면 항상 같은 결과를 반환하는 특징
    - return결과가 내용은 동일하지만 가변객체라서 결론적으로 다른 객체라고 해도 참조적으로 투명한 것으로 간주한다.
3. 객체지향 프로그래밍과 함수형 프로그래밍
    - 자바에서는 함수형 프로그래밍을 익스트림 객체지향 프로그래밍의 일부로 간주한다.
4. 함수형 실전 연습
    - List<Integer>가 주어졌을 때 이것의 모든 부분집합으로 구성된 List<List<Integer>>를 만드는 예제
        - {1, 4, 9} -> {{1}, {4}, {9}, {1, 4}, {1, 9}, {4, 9}, {1, 4, 9}}
    - 이 예제는 왜 함수형이라고 할 수 있는가?
        - 원본 List나 다른 데이터에 영향을 주는 부분이 없기 때문
        - 몇 번을 반복하든 인수가 동일하면 결과 또한 항상 동일하기 때문(참조 투명성)
        ```java
        @Slf4j
        public class Ch18FunctionalProgramming {
        
          @Test
          void divideList() {
            List<Integer> originList = Arrays.asList(1, 4, 9);
            List<List<Integer>> subList = subsets(originList);
            subList.forEach(list -> log.info("list : {}", list));
          }
        
          // 부분집합을 가른다
          private static List<List<Integer>> subsets(List<Integer> list) {
            log.info("list subsets : {}", list);
            if (list.isEmpty()) {
              List<List<Integer>> emptyResult = new ArrayList<>();
              emptyResult.add(Collections.emptyList());
              return emptyResult;
            }
            Integer firstInteger = list.get(0);
            List<Integer> rest = list.subList(1, list.size());
            // 빈 리스트가 아니면 먼저 하나의 요소를 꺼내고
            // 나머지 요소의 서브집합을 찾아서 subResult로 전달한다.
            // subResult는 절반의 정답을 포함한다.
            List<List<Integer>> subResult = subsets(rest);
            log.info("sub result : {}", subResult);
            // 처음 꺼내놨던 요소를 subResult의 요소마다 firstInteger를 하나씩 붙인 요소들을 포함한다. 
            List<List<Integer>> subResultFirstIntegerAdded = insertAll(firstInteger, subResult);
            // 두 subResult를 합친다.
            return concat(subResult, subResultFirstIntegerAdded);
          }
        
          public static <T> List<List<T>> insertAll(T firstInteger, List<List<T>> lists) {
            log.info("insertAll progress. firstInteger : {}, list : {}", firstInteger, lists);
            List<List<T>> result = new ArrayList<>();
            for (List<T> list : lists) {
              List<T> copyList = new ArrayList<>();
              copyList.add(firstInteger);
              copyList.addAll(list);
              result.add(copyList);
            }
            return result;
          }
        
          static <T> List<List<T>> concat(List<List<T>> a, List<List<T>> b) {
            log.info("concat a : {}, b : {}", a, b);
            List<List<T>> r = new ArrayList<>(a);
            r.addAll(b);
            return r;
          }
        
        }
        ```
        ```
        위 예제 로그
        list subsets : [1, 4, 9]
        list subsets : [4, 9]
        list subsets : [9]
        list subsets : []
        sub result : [[]]
        insertAll progress. firstInteger : 9, list : [[]]
        concat a : [[]], b : [[9]]
        sub result : [[], [9]]
        insertAll progress. firstInteger : 4, list : [[], [9]]
        concat a : [[], [9]], b : [[4], [4, 9]]
        sub result : [[], [9], [4], [4, 9]]
        insertAll progress. firstInteger : 1, list : [[], [9], [4], [4, 9]]
        concat a : [[], [9], [4], [4, 9]], b : [[1], [1, 9], [1, 4], [1, 4, 9]]
        list : []
        list : [9]
        list : [4]
        list : [4, 9]
        list : [1]
        list : [1, 9]
        list : [1, 4]
        list : [1, 4, 9] 
        ```

### 3. 재귀와 반복
> 순수한 함수형 프로그래밍 언어에서 반복문은 불변성을 저해할 수 있기 때문에 while, for등의 약속어가 없다.
> 반복문 대신 변화가 일어나지 않는 재귀를 이용한다.
```
@Slf4j
public class Ch18FunctionalProgramming {
  // 반복 방식의 팩토리얼과 재귀 방식, 그리고 스트림 방식 팩토리얼
  @Test
  void factorials() {
    log.info("iterative : {}", factorialIterative(10));
    log.info("recursive : {}", factorialRecursive(10L));
  }

  private int factorialIterative(int n) {
    int r = 1;
    for (int i = 1; i <= n; i++) {
      r *= i;
    }
    return r;
  }

  private long factorialRecursive(long n) {
    return n == 1 ? 1 : n * factorialRecursive(n - 1);
  }

  private long factorialStreams(long n) {
    return LongStream.rangeClosed(1, n)
            .reduce(1, (long a, long b) -> a * b);
  }
  
}
```
- 재귀 메서드 주의
    - 메서드를 호출할 때 마다 생성되는 정보를 저장할 새로운 스택 프레임이 만들어진다.
    - 따라서 큰 입력값을 사용하면 StackOverflow가 발생할 수 있다.
    - 일반적으로 반복코드 보다 재귀코드가 더 비싸다
- 꼬리 호출 최적화
    - 함수형언어에서 제공하는 재귀코드의 문제를 해결하기 위한 방법
    - 컴파일러가 하나의 스택 프레임을 재활용할 가능성이 생긴다.
    - 스트림으로 대체하면 됩니당
    
### 4. 마치며
- 공유된 가변 자료구조를 줄이는 것은 유지보수에 도움이 된다.
- 함수형 프로그래밍은 부작용이 없는 메서드와 선언형 프로그래밍 방식을 지향한다.
- 함수형 메서드는 입력 인수와 출력 결과만을 갖는다.
- 같은 인수값으로 함수를 호출했을 때 항상 같은 값을 반환하면 참조 투명성을 갖는 함수다. 
- while루프 같은 반복문은 재귀로 대체할 수 있다.
- 자바에서는 고전 방식의 재귀보다는 꼬리 재귀를 사용해야 추가적인 컴파일러 최적화를 기대할 수 있다.