package com.mj.modernjava.ch19;

import java.util.HashMap;
import java.util.Map;
import java.util.function.DoubleUnaryOperator;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;
import org.junit.jupiter.api.Test;

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

  @Test
  public void whatMeaningJourneyIs() {
    TrainJourney busan = new TrainJourney("Busan");
    TrainJourney gumiToNothing = new TrainJourney("GumiToNothing", "Gumi", "", 0, null);
    TrainJourney seoulToGumi = new TrainJourney("SeoulToBusan", "Seoul", "Gumi", 5000, gumiToNothing);
    log.info("seoulToGumi :{}", seoulToGumi);
    log.info("seoulToGumi onWard :{}", seoulToGumi.getOnWard());
  }

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

  @Ignore
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

    share.add(img);
    share.add(img2);

    root.add(share);
    root.add(bin);
    root.accept(new ViewVisitor());
    // /root
    // /root/conf
    // /root/bin
    // /root/bin/readme
    // /root/bin/share
    // /root/bin/share/img
    // /root/bin/share/img2
  }

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

  @Test
  public void combineFunctionsTest() {
    Integer result = Combinators
        // x -> (2 * (2 * (2 * x) ) ) 또는 x -> 8*x
        .repeat(3, (Integer x) -> 2 * x)
        .apply(10);
    log.info("result :{}", result);
  }
}
