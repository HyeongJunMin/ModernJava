# 목차

> 요약 및 결론  
> 책 내용  
> 컬렉션 리터럴  
> for-each 반복문은 내부적으로 Iterator 객체를 사용한다


---

#### 요약 및 결론

---

#### 책 내용

1.  리팩터링
    -   가독성과 유연성을 개선하기 위함
    1.  코드 가독성 개선
        -   일반적으로 어떤 코드를 다른 사람도 쉽게 이해할 수 있음을 의미
    2.  익명 클래스를 람다 표현식으로 리팩터링 하기
        -   익명 클래스는 추상메서드를 쉽게 구현할 수 있는 기능이지만, 코드가 보기싫게 생겼다
        -   그러므로 람다 표현식으로 리팩토링 한다
        -   하지만 모든 익명클래스를 람다 표현식으로 변환할 수는 없다.
            -   익명클래스에서 사용한 this와 super는 람다표현식에서 다른 의미를 갖는다.
                -   익명클래스에서 this : 익명클래스 자신
                -   람다에서 this : 람다를 감싸는 클래스
                -   super도 그런식       
            -   익명클래스는 익명클래스를 감싸고 있는 클래스의 변수를 가릴 수 있다.
                -  람다는 안됨
                -   아래 예제에 showme
            -   익명클래스를 람다로 바꾸면 콘텍스트 오버로딩에 따른 모호함이 초래될 수 있음
                -   익명클래스는 인스턴스화할 때 명시적으로 형식이 정해짐
                -   람다의 형식은 콘텍스트에 따라 달라짐 
                ~~~java
                public class RefactoringTest {
                    @Test
                      public void differenceOfAnonymousClassAndLambdaTest() {
                        SuperTest superTestAno = new SuperTest() {
                          public int superReturn() {
                            //  superreturn : com.mj.modernjava.ch9.RefactoringTest$2
                            log.debug("superreturn : {}, super : {}", super.getClass().getName(), super.toString());
                            return 10;
                          }
                        };
                        SuperTest superTestLamb = () -> {
                          log.debug("super class : {}, super : {}", super.getClass().getName(), super.toString());
                          return 1;
                        };
                        superTestAno.superReturn();
                        superTestLamb.superReturn();
                        log.debug("this class : {}, this : {}", this.getClass().getName(), this.toString());
                        //  superreturn : com.mj.modernjava.ch9.RefactoringTest$2, super : com.mj.modernjava.ch9.RefactoringTest$2@3c0be339
                        //  super class : com.mj.modernjava.ch9.RefactoringTest, super : com.mj.modernjava.ch9.RefactoringTest@15ca7889
                        //  this class : com.mj.modernjava.ch9.RefactoringTest, this : com.mj.modernjava.ch9.RefactoringTest@15ca7889
                      }
                }
                ~~~
    3.  람다 표현식을 메서드 참조로 리팩터링 하기
        -   메서드 참조가 람다보다 더 보기 쉬우니까
            ~~~
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
            ~~~
    4.  명령형 데이터 처리를 스트림으로 리펙터링하기
        -   컬렉션 반복 코드는 모두 스트림 API로 바꾸는게 좋다
            -   스트림 API는 데이터 처리 파이프라인의 의도를 더 명확하게 보여주기 때문
        -   명령형 코드를 스트림 API로 바꾸는 것은 쉬운 일이 아니다.
            -   복잡한 코드면 그럴 수 있겠다.
            -   책에 있던 툴 사이트는 접속 안되고 비슷한 툴 못찾겠다
    5.  코드 유연성 개선
        -   변화하는 요구사항에 유연하게 대응할 수 있는 코드 구현
        -   함수형 인터페이스 적용
            -   람다 표현식을 이용하기 위함
        1.  조건부 연기 실행(Conditional Deferred Execution)
            -   실제 작업을 처리하는 코드 내부에 제어 흐름문이 복잡하게 얽힌 코드를 흔히 볼 수 있다.
            -   레벨을 매개변수로 받아서 레벨에 맞으면 남기고 레벨에 안맞으면 안남기는 형식의 메소드를 예로 들었음
                -   괜찮은 방법인듯 한데 Slf4j쓰니까 쓸 일 없다.
                -   Slf4j랑 비교해보자면 메서드를 다르게 가져가는게 작성하기 더 힘들더라도 읽기는 더 편할듯 하다
                ~~~
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
                  public void log(Level level, Supplier<String> msgSupplier){
                    if(logger.isLoggable(level)) {
                      log(level, msgSupplier.get());
                    }
                  }
                ~~~
        2.  실행 어라운드(Excute Around)
            -   매번 같은 준비, 종료 과정을 반복적으로 수행하는 코드는 람다로 변환할 수 있다.
            -   준비, 종료 과정을 처리하는 로직을 재사용 하는 방법으로.
            ~~~
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
            ~~~
        
2.  람다로 객체지향 디자인 패턴 리팩터링 하기
    -   디자인패턴?
        -   공통적인 소프트웨어 문제를 설계할 때 재사용할 수 있는 검증된 청사진 제공
        -   특정 케이스에서 효율이 검증된 문제 해결 방법
    1.  전략 패턴
        -   한 유형의 알고리즘을 보유한 상태에서 런타임에 적절한 알고리즘을 선택하는 기법
        
        
3.  개선된 ConcurrentHashMap
    -   특징

---

#### 컬렉션 리터럴

```
//파이썬에서
//출처 : https://wikidocs.net/20562
[ ... ]로 감싸져 있으면 list자료형
( ... )로 감싸져 있으면 tuple자료형
{ 키:값, ... }로 감싸져 있으면 dictionary자료형
{ ... }로 감싸져 있으면 set자료형
```

---

#### for-each 반복문은 내부적으로 Iterator 객체를 사용한다

그렇다