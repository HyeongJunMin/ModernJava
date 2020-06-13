package com.mj.modernjava.ch9;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.groupingBy;

import com.mj.modernjava.common.Apple;
import com.mj.modernjava.common.Apple.Colors;
import com.mj.modernjava.common.CommonService;
import com.mj.modernjava.common.Dish;
import com.mj.modernjava.common.Dish.CaloricalLevel;
import com.mj.modernjava.common.Menu;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.StopWatch;

@Slf4j
public class RefactoringTest {

  @Autowired
  private CommonService commonService;

  @Test
  public void reactTest() {
    List<Apple> appleList = new ArrayList<>(100_000);
    StopWatch stopWatch = new StopWatch();
    stopWatch.start();
    IntStream.range(1, 100_000).forEach(i -> {
      synchronized (appleList) {
        appleList.add(new Apple(Colors.GREEN, i));
      }
    });
    log.debug("appleList size : {}", appleList.size());
    stopWatch.stop();
    System.out.println(stopWatch.getTotalTimeMillis());
  }

  @Async
  public void send(int a) {
    log.debug("a : {}", a);
  }

  @Test
  public void comparingMonthByNameAndNumericTest() {
    //람다표현식을 이용한 이름으로 숫자찾기 예제
    log.debug("result : {}", MonthNames.valueOf("JAN").getPredicate());
  }

  @Getter
  @AllArgsConstructor
  public enum MonthNames {
    JAN(1, (monthNames) -> monthNames.getValue() == 1),
    FEB(2, (monthNames) -> monthNames.getValue() == 1),
    MAR(3, (monthNames) -> monthNames.getValue() == 1)
    ;
    private int value;
    private Predicate<MonthNames> predicate;
  }

  @Test
  public void differenceOfAnonymousClassAndLambdaTest() {
    int showme = 1;
    SuperTest superTestAno = new SuperTest() {
      public int superReturn() {
        int showme = 10;
        log.debug("superreturn : {}, super : {}", super.getClass().getName(), super.toString());
        return 10;
      }
    };
    SuperTest superTestLamb = () -> {
//       int showme = 100; variable 'showme' is already defined 뜸
      log.debug("super class : {}, super : {}", super.getClass().getName(), super.toString());
      return 1;
    };
    superTestAno.superReturn();
    superTestLamb.superReturn();
    log.debug("this class : {}, this : {}", this.getClass().getName(), this.toString());
    //  익명클래스 - 람다 super, this 차이
    //  superreturn : com.mj.modernjava.ch9.RefactoringTest$2, super : com.mj.modernjava.ch9.RefactoringTest$2@3c0be339
    //  super class : com.mj.modernjava.ch9.RefactoringTest, super : com.mj.modernjava.ch9.RefactoringTest@15ca7889
    //  this class : com.mj.modernjava.ch9.RefactoringTest, this : com.mj.modernjava.ch9.RefactoringTest@15ca7889
  }

  @Test
  public void lambdaToMethodReferenceTest() {
    List<Dish> menu = Menu.getList();
    // 조건문들이 어디있는가에 차이가 있을 뿐이지만 확실히 보기 좋다
    //람다
    Map<CaloricalLevel, List<Dish>> dishes =
        menu.stream().collect(groupingBy(dish -> {
        if (dish.getCalories() <= 400) return CaloricalLevel.DIET;
        else if (dish.getCalories() <= 700) return CaloricalLevel.NORMAL;
        else return CaloricalLevel.FAT;
      }));
    //메서드참조
    menu.stream().collect(groupingBy(Dish::getCaloricalLevel));

    menu.sort((Dish d1, Dish d2) -> d2.getCalories().compareTo(d1.getCalories()));
    log.debug("menu list : {}", menu);
    menu.sort(comparing(Dish::getCalories));
    log.debug("menu list : {}", menu);
  }

  Logger logger = Logger.getLogger(this.getClass().getName());

  @Test
  public void enhanceCodeFlexibilityTest() {
    //logger의 상태가 isLoggable이라는 메서드에 의해 클라이언트 코드로 노출된다.
    //메시지를 로깅할 때 마다 logger객체의 상태를 확인하는 건 코드를 어지럽힐 뿐이다.
    Logger logger = Logger.getLogger(this.getClass().getName());
    if (logger.isLoggable(Level.FINER)) {
      logger.finer("Problem : {}");
    }
  }
  //아래와 같은 메소드를 정의하는게 좋단다
//  public void log(Level level, Supplier<String> msgSupplier){
//    if(logger.isLoggable(level)) {
//      log(level, msgSupplier.get());
//    }
//  }

  @Test
  public void executionAroundTest() throws IOException {
    String file = "/Users/hjmin/study/ModernJava/src/main/resources/static/sampleData/ch9sample.txt";
    //  람다로 BufferedReader객체의 동작을 결정(1개 줄을 읽을지, 2개 줄을 읽을지)할 수 있는 이유는 함수형 인터페이스 BufferedReaderProcessor가 있기 때문
    String oneLine = processFile((BufferedReader b) -> b.readLine(), file);
    String twoLines = processFile((BufferedReader b) -> String.format("%s, %s", b.readLine(), b.readLine()), file);
    log.debug("oneLine : {}", oneLine); //  oneLine : sample line 1
    log.debug("twoLines : {}", twoLines); //  twoLines : sample line 1, sample line 2
  }
  public static String processFile(BufferedReaderProcessor p, String filePath) throws IOException {
    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
      return p.process(br);
    }
  }
  public interface BufferedReaderProcessor {
    String process(BufferedReader b) throws IOException;
  }
}
