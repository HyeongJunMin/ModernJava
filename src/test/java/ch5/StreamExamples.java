package ch5;

import static java.util.stream.Collectors.toList;

import common.Dish;
import common.Dish.Type;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@Slf4j
public class StreamExamples {

  List<Dish> menu = new ArrayList();


  @BeforeAll
  public void setUp() {
    menu.add(new Dish("pork", false, 800, Type.MEAT));
    menu.add(new Dish("beef", false, 700, Type.MEAT));
    menu.add(new Dish("chicken", false, 400, Type.MEAT));
    menu.add(new Dish("french fries", true, 530, Type.OTHER));
    menu.add(new Dish("rice", true, 350, Type.OTHER));
    menu.add(new Dish("season fruits", true, 120, Type.OTHER));
    menu.add(new Dish("pizza", true, 550, Type.OTHER));
    menu.add(new Dish("prawns", false, 300, Type.FISH));
    menu.add(new Dish("salmon", false, 450, Type.FISH));
  }

  @Test
  public void filterTest() {
    //프리디케이트로 필터링
    List<Dish> vegeMenu = menu.stream()
        .filter(Dish::isVegeterian)
        .collect(toList());

    //고유 요소(distinct) 필터링. 출력결과 : 2, 4
    Arrays.asList(1, 2, 1, 3, 3, 2, 4).stream()
        .filter(i -> i % 2 == 0)
        .distinct()
        .forEach(System.out::println);
  }

  @Test
  public void slicingTest() {

    //칼로리순으로 정렬
    menu.sort(Comparator.comparing(Dish::getCalories));

    //predicate가 참이면 반복 종료(Java9부터 가능)
//    menu.stream()
//        .takeWhile(dish -> dish.getCalories < 320)
//        .collect(toList());

    //predicate가 거짓이면 반복 종료(Java9부터 가능)
//    menu.stream()
//        .dropWhile(dish -> dish.getCalories > 300)
//        .collect(toList());

    //스트림 축소
    menu.stream()
        .limit(3);

    //요소 건너뛰기. 첫 2개 요소는 수행하지 않음. 요소가 n개 이하인 스트림에 skip(n)호출하면 빈 스트림 반환
    menu.stream()
        .skip(2);
  }

  @Test
  public void mappingTest() {
    log.debug("start");
    //Dish객체 리스트를 문자열 리스트로 매핑(변환)
    List<String> collect = menu.stream()
        .map(Dish::getName)
        .collect(toList());

    //flatMap : 스트림의 각 값을 다른 스트림으로 만든 다음 모든 스트림을 하나의 스트림으로 연결하는 기능
    List<String> words = Arrays.asList("Hello", "World");
    List<String> uniqueChar = words.stream()
        .map(word -> word.split(""))  //각 단어를 개별 문자를 포함하는 배열로 변환
        .flatMap(Arrays::stream)  //생성된 스트림을 하나의 스트림으로 평면화
        .distinct()
        .collect(toList());
    uniqueChar.stream().forEach(System.out::print);
  }

  @Test
  public void searchingAndMatchingTest() {
    //요소 검색
    Optional<Dish> any = menu.stream()
        .filter(Dish::isVegeterian)
        .findAny();
  }

  @Test
  public void reducingTest() {
    //초기값 0을 주면 첫 번째 람다연산의 a는 초기값 0이되고 b는 배열의 첫 번째 요소 1이됨
    List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
    int sum = numbers.stream().reduce(0, (a, b) -> a + b);

    //초기값을 주지 않으면 Optional을 반환
    Optional<Integer> reduce = numbers.stream().reduce((a, b) -> a + b);

    //최대값과 최소값 : 스트림의 모든 요소를 소비할 때 까지 람다를 반복수행
    Optional<Integer> reduce1 = numbers.stream().reduce(Integer::max);
    Optional<Integer> reduce2 = numbers.stream().reduce(Integer::min);
  }

  @Test
  public void primitiveStreamSpecializationTest() {
    //숫자 스트림으로 매핑
    int sum = menu.stream()
        .mapToInt(Dish::getCalories)
        .sum();//reduce 대신 사용가능

    //객체 스트림으로 복원
    IntStream intStream = menu.stream().mapToInt(Dish::getCalories);
    Stream<Integer> boxed = intStream.boxed();//숫자 스트림을 스트림으로 변환
  }

  @Test
  public void streamRangeTest() {
    //시작값과 종료값을 포함하지 않음
    IntStream range = IntStream.range(1, 100).filter(n -> n % 2 == 0);

    //시작값과 종료값을 포함
    IntStream intStream = IntStream.rangeClosed(1, 100).filter(n -> n % 2 == 0);
  }

  @Test
  public void makeStreamTest() {
    //값으로 스트림 만들기
    Stream<String> valueStream = Stream.of("Modern", "Java", "In", "Action");
    valueStream.map(String::toUpperCase).forEach(System.out::print);

    //배열로 스트림 만들기
    int[] numbers = {2, 3, 5, 7, 11, 13};
    int sum = Arrays.stream(numbers).sum();

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

    //초기값과 람다를 인수로 받아 무한 스트림을 만드는 iterate 메서드
    Stream.iterate(0, n -> n+2).limit(10).forEach(System.out::println);
    //iterate메서드로 피보나치수열 구현
    Stream.iterate(new int[]{0, 1}, t -> new int[]{t[1], t[0] + t[1]})
        .limit(20)
        .forEach(t -> System.out.println("(" + t[0] + ", " + t[1] + ")"));

    //iterate와 달리 생산된 각 값을 연속적으로 계산하지 않고 무한스트림을 만드는 generate 메서드
    Stream.generate(Math::random).limit(5).forEach(System.out::println);
    IntStream infiniteIntStream = IntStream.generate(() -> 1);

  }

}
