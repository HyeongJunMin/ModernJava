# 목차

> 요약 및 결론  
> 책 내용  
> 중간 연산과 최종 연산
> 쇼트서킷

---

#### 요약 및 결론
예제 코드들이 많이 나왔기 때문에 필요할 때 빠르게 찾기 좋은 글로 만들어야 된다

---

#### 책 내용

0.  세팅
    
    ```
    List<Dish> menu = new ArrayList();
    menu.add(new Dish("pork", false, 800, Type.MEAT));
    menu.add(new Dish("beef", false, 700, Type.MEAT));
    menu.add(new Dish("chicken", false, 400, Type.MEAT));
    menu.add(new Dish("french fries", true, 530, Type.OTHER));
    menu.add(new Dish("rice", true, 350, Type.OTHER));
    menu.add(new Dish("season fruits", true, 120, Type.OTHER));
    menu.add(new Dish("pizza", true, 550, Type.OTHER));
    menu.add(new Dish("prawns", false, 300, Type.FISH));
    menu.add(new Dish("salmon", false, 450, Type.FISH));
    ```
    
1.  필터링 : 요소를 선택하는 방법
    
    ```
    //프리디케이트로 필터링
    List<Dish> vegeMenu = menu.stream()
    .filter(Dish::isVegeterian)
    .collect(toList());
    ```
    
    ```
    //고유 요소(distinct) 필터링. 출력결과 : 2, 4 
    Arrays.asList(1, 2, 1, 3, 3, 2, 4).stream()
    .filter(i -> i % 2 == 0)
    .distinct()
    .forEach(System.out::println);
    ```
    
2.  스트림 슬라이싱 : 요소를 선택하거나 스킵하는 방법
    
    ```
    //predicate가 참이면 반복 종료(Java9부터 가능)  
    menu.sort(Comparator.comparing(Dish::getCalories));  
    menu.stream()  
    .takeWhile(dish -> dish.getCalories < 320)  
    .collect(toList());  
    ```
    
    ```
    //predicate가 거짓이면 반복 종료(Java9부터 가능)  
    menu.sort(Comparator.comparing(Dish::getCalories));
    menu.stream()  
    .dropWhile(dish -> dish.getCalories > 300)  
    .collect(toList());  
    ```
    
    ```
    //스트림 축소  
    menu.stream()  
    .limit(3);  
    ```
    
    ```
    //요소 건너뛰기. 첫 2개 요소는 수행하지 않음. 요소가 n개 이하인 스트림에 skip(n)호출하면 빈 스트림 반환  
    menu.stream()  
    .skip(2);  
    ```
    
3.  매핑 : 특정 데이터를 선택하는 기능
    

-   기존 값을 수정하는게 아닌 새로운 버전을 만든다는 개념
    
    ```
    //Dish객체 리스트를 문자열 리스트로 매핑(변환)
    List<String> collect = menu.stream()
       .map(Dish::getName)
       .collect(toList());
    ```
    
    ```
    //flatMap : 스트림의 각 값을 다른 스트림으로 만든 다음 모든 스트림을 하나의 스트림으로 연결하는 기능
    List<String> words = Arrays.asList("Hello", "World");
    List<String> uniqueChar = words.stream()  //각 단어를 개별 문자를 포함하는 배열로 변환
        .map(word -> word.split(""))  //생성된 스트림을 하나의 스트림으로 평면화
        .flatMap(Arrays::stream)
        .distinct()
        .collect(toList());
    uniqueChar.stream().forEach(System.out::print);
    ```
    

4.  검색과 매칭 : 특정 속성이 데이터 집합에 있는지 여부를 검색

-   allMatch, anyMatch, noneMatch, findFirst, findAny
    
-   쇼트서킷을 이용해서 결과를 찾는 즉시 실행을 종료함(스트림의 최적화)
    
-   병렬실행에서는 첫 번째 요소를 찾기 어렵기 때문에 findAny, findFirst를 사용
    
    ```
    //요소 검색
    Optional<Dish> any = menu.stream()
       .filter(Dish::isVegeterian)
       .findAny();
    ```
    

5.  리듀싱

-   더욱 복잡한 질의를 수행하기 위한 기능
    
-   종이가 작은 조각이 되도록 반복해서 접는것과 비슷하다는 의미로 폴드라고 부름
    
-   reduce는 두 개의 인수를 가짐
    
    -   초기값 0
    -   두 요소를 조합해서 새로운 값을 만드는 BinaryOperator
-   장점 : 내부 반복이 추상화되면서 내부 구현에서 **병렬**로 reduce를 **실행**할 수 있게 됨
    
    ```
    //초기값 0을 주면 첫 번째 람다연산의 a는 초기값 0이되고 b는 배열의 첫 번째 요소 1이됨
    List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
    int sum = numbers.stream().reduce(0, (a, b) -> a + b);
    ```
    
    ```
    //초기값을 주지 않으면 Optional을 반환
    Optional<Integer> reduce = numbers.stream().reduce((a, b) -> a + b);
    ```
    
    ```
    //최대값과 최소값 : 스트림의 모든 요소를 소비할 때 까지 람다를 반복수행
    Optional<Integer> reduce1 = numbers.stream().reduce(Integer::max);
    Optional<Integer> reduce2 = numbers.stream().reduce(Integer::min);
    ```
    

6.  실전 연습

7.  숫자형 스트림
    
    ```
    //숫자 스트림으로 매핑
    int sum = menu.stream()
      .mapToInt(Dish::getCalories)
      .sum();//reduce 대신 사용가능
    ```
    
    ```
    //객체 스트림으로 복원
    IntStream intStream = menu.stream().mapToInt(Dish::getCalories);
    Stream<Integer> boxed = intStream.boxed();//숫자 스트림을 스트림으로 변환
    ```
    
    ```
    //시작값과 종료값을 포함하지 않음
    IntStream range = IntStream.range(1, 100).filter(n -> n % 2 == 0);
    ```
    
    ```
    //시작값과 종료값을 포함
    IntStream intStream = IntStream.rangeClosed(1, 100).filter(n -> n % 2 == 0);
    ```

8.	스트림 만들기
-	값, 배열, 파일 등 다양한 방식으로 스트림을 만드는 방법
-	무한스트림 만드는 방법
	-	명시적으로 스트림의 크기를 제한(limit)하지 않으면 최종 연산을 수행했을 때 아무 결과도 계산되지 않는다.
    -	무한스트림의 요소는 무한적으로 계산이 반복되므로 정렬하거나 reduce할 수 없다.
	```
    //값으로 스트림 만들기
    Stream<String> valueStream = Stream.of("Modern", "Java", "In", "Action");
    valueStream.map(String::toUpperCase).forEach(System.out::print);
    ```
	```
    //배열로 스트림 만들기
    int[] numbers = {2, 3, 5, 7, 11, 13};
    int sum = Arrays.stream(numbers).sum();
    ```    
    ```
    //파일로 스트림 만들기
    long uniqueWords = 0;
    try (Stream<String> lines =
        Files.lines(Paths.get("data.txt"), Charset.defaultCharset())) { //스트림은 AutoClosable이므로 try-finally 필요없음
      uniqueWords =
          lines.flatMap(line -> Arrays.stream(line.split(" ")))
              .distinct()
              .count();
    } catch (IOException e) {
    }
    ```    
    ```
    //초기값과 람다를 인수로 받아 무한 스트림을 만드는 iterate 메서드
    Stream.iterate(0, n -> n+2).limit(10).forEach(System.out::println);
    //iterate메서드로 피보나치수열 구현
    Stream.iterate(new int[]{0, 1}, t -> new int[]{t[1], t[0] + t[1]})
        .limit(20)
        .forEach(t -> System.out.println("(" + t[0] + ", " + t[1] + ")"));
    ```    
    ```
    //iterate와 달리 생산된 각 값을 연속적으로 계산하지 않고 무한스트림을 만드는 generate 메서드
    Stream.generate(Math::random).limit(5).forEach(System.out::println);
    IntStream infiniteIntStream = IntStream.generate(() -> 1);
    ```





---

#### 중간 연산과 최종 연산

-   중간 연산
    -   filter, distinct, takeWhile, dropWhile, skip, limit, map, flatMap, sorted
-   최종 연산
    -   anyMatch, noneMatch, allMatch, findAny, findFirst, forEach, collect, reduce, count

---

#### 쇼트서킷

전체 스트림을 처리하지 않아도 결과를 반환할 수 있게 해주는 원리  
예) 여러 and 연산으로 연결된 커다란 불리언 표현식에서 하나라도 거짓이라는 결과가 나오면 나머지 표현식의 결과와 상관 없이 전체 결과도 거짓이 됨 -> 이러한 상황을 **쇼트서킷**이라 부름
