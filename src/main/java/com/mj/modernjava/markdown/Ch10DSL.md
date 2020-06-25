# 목차

> 요약 및 결론  
> 책 내용  

---

## 요약 및 결론
자바 8의 람다와 메서드참조 기능의 추가로 DSL 개발 적합도가 얼마나 향상됐는지 보여주기 위한 챕터인듯 했다.

하지만 가장 좋았던건 편하게 쓰기만 하던 메소드 체인 패턴(플루언트 스타일)이 어떻게 구성됐는지 알게된 것이다. 자바 좋다.

---

## 책 내용

###1.  도메인 전용 언어(Domain Specific Languages)
-   DSL은 특정 비즈니스 도메인의 문제를 해결하려고 만들어진 특수 프로그래밍 언어이다.
-   특정 비즈니스 도메인을 인터페이스로 만든 API라고 생각할 수 있다.
-   직역 : 영역 별 언어
#####1.1. DSL의 장점과 단점
장점과 단점에 대한 이유들과 사례들은 10장이 진행되며 하나씩 나온다.
1.  장점 : 
    1.  간결함 : API는 비즈니스 로직을 간편하게 캡슐화하므로 반복을 피할 수 있고 코드를 간결하게 만들 수 있다. 
    2.  가독성 : 도메인 영역의 용어를 사용하므로 비 도메인 전문가도 코드를 쉽게 이해할 수 있다.
    3.  유지보수 : 잘 설계된 DSL로 구현한 코드는 유지보수가 쉽다.
    4.  높은 수준의 추상화 : 도메인의 문제와 직접적으로 관련되지 않은 세부 사항을 숨긴다.
    5.  집중 : 목적이 명확한 언어이므로 프로그래머가 특정 코드에 집중할 수 있어서 생산성이 좋아진다.
    6.  관심사 분리 : 애플리케이션과는 독립적으로, 비즈니스 관련된 코드에만 집중하기가 쉽다.
2.  단점
    1.  DSL 설계의 어려움 : 간결하게 제한적인 언어에 도메인 지식을 담는 것은 어렵다.
    2.  개발 비용 : 코드에 DSL을 추가하는 작업은 초기 프로젝트에 많은 비용과 시간이 소모되는 작업이다. DSL 유지보수와 변경은 프로젝트에 부담을 준다.
    3.  추가 우회 계층 : DSL은 추가적인 계층으로 도메인 모델을 감싸며 이 때 계층을 최대한 작게 만들어 성능 문제를 회피한다. -> 이게 왜 단점이지?
    4.  새로 배워야 하는 언어 : 여러 비즈니스 도메인을 다루는 개별 DSL을 사용하는 상황에서 개별 DSL이 독립적으로 진화하게 된다면 복잡해진다.
    5.  호스팅 언어 한계 : 장황하고 엄격한 문법을 가진 프로그래밍 언어를 기반으로 만든 DSL은 문법 제약 때문에 가독성이 떨어질 수 있다.
#####1.2. JVM에서 이용할 수 있는 다른 DSL 해결책
1.  내부 DSL : 호스팅 언어를 기반으로 구현
    -   자바 문법 때문에 읽기 쉽고 간단한 DSL을 만드는데 한계가 있었지만 람다 표현식의 등장으로 어느정도 해결되었다.
    -   익명 내부 클래스에는 신호대비 잡음이 있을 수 있는데 람다 표현식 또는 메소드참조로 해결할 수 있다.
    ~~~
    @Test
    public void innerDSLTest() {
        List<String> numbers = Arrays.asList("one", "two", "three");
        //익명 내부 클래스
        numbers.forEach(new Consumer<String>() {
            @Override
            public void accept(String s) {
                log.debug(s);
            }
        });
        //메소드 참조
        numbers.forEach(log::debug);
    }
    ~~~
    -   순수 자바로 DSL을 구현할 때 얻는 장점
        1.  외부 DSL을 배우기 위한 노력을 하지 않아도 된다.
        2.  다른 언어의 컴파일러를 이용하거나 외부 DSL 도구를 사용할 필요 없이 자바 코드와 DSL을 한번에 컴파일 할 수 있다.
        3.  기존 자바 IDE의 자동완성 기능을 그대로 사용할 수 있다.
        4.  추가로 DSL을 쉽게 기존 코드로 합칠 수 있다. 
2.  다중 DSL : 자바가 아니지만 JVM에서 실행됨(스칼라, 그루비 등)
    -   JVM에서 실행되는 언어 중에 문법이 간편하고 제약이 적은 언어가 많다.
    -   Scala에서 내장DSL로 3번 Hello World를 출력하는 프로그램 구현
        -   문법적 잡음이 없음을 확인
    ~~~
    //  주어진 함수 f를 주어진 횟수만큼 반복 실행하는 유틸리티 함수 구현
    def times(i: Int, f: => Unit): Unit = { f
        if (i > 1) timesStandard(i - 1, f)
    }
    times(3, pringln("Hello World"))
    ~~~
    -   단점
        -   새로운 프로그래밍 언어를 배워야만 한다.
        -   두 개 이상의 언어가 혼재하므로 여러 컴파일러로 소스를 빌드하도록 빌드 과정을 개선해야 한다.
        -   JVM에서 실행되는 거의 모든 언어가 자바와 100% 호환을 주장하고 있지만 완벽하지 않을 때가 많다.
            -   스칼라와 자바 컬렉션은 호환되지 않으므로 상호 컬렉션을 전달하려면 기존 컬렉션을 대상 언어의 API에 맞게 변환해야 한다.
3.  외부 DSL(Stand Alone) : 호스팅 언어와는 독립적으로 자체의 문법을 가짐
    -   시간과 노력이 많이 들어간다.
        -   새 언어 파싱 -> 파서 결과 분석 -> 외부 DSL을 실행할 코드 작성
    -   외부 DSL의 장점
        1.  무한한 유연성 : 필요한 특성을 완벽하게 제공하는 언어를 설계할 수 있음
        2.  기존 어플리케이션과 분리 :DSL과 호스트 언어 사이에 인공계층이 생기므로 단점이 될 수도 있다. 
###2.  최신 자바 API의 작은 DSL
*   자바의 새로운 기능의 장점을 적용한 첫 API는 네이티브 자바 API 자신이다.
*   람다 표현식과 메소드 참조를 이용해 DSL의 가독성, 재사용성, 결합성이 높아졌다.
#####2.1. 스트림 API는 컬렉션을 조작하는 DSL
-   Stream API는 작지만 강력한 내부 DSL : 데이터 조작(필터링, 정렬, 변환, 그룹화 등) 기능 제공
-   Stream API의 플루언트 형식은 잘 설계된 DSL의 또 다른 특징
    -   모든 중간 연산은 게으르며 다른 연산으로 파이프라인될 수 있는 스트림으로 반환된다.
    -   최종 연산은 적극적이며 전체 파이프라인이 계산을 일으킨다.
#####2.2. 데이터를 수집하는 DSL인 Collectors
-   Collectors는 다중 수준 그룹화를 달성할 수 있도록 합쳐질 수 있다.
    ~~~
    @Test
    public void multipleGrouping() {
        List<Dish> menuList = Menu.getList();
        Map<Dish.CaloricalLevel, Map<Dish.Type, List<Dish>>> collect
                = menuList.stream().collect(groupingBy(Dish::getCaloricalLevel, groupingBy(Dish::getType)));
        log.debug("result : {}", collect.toString());
        //result : {
        // FAT={MEAT=[Dish(name=pork, vegeterian=false, calories=800, type=MEAT)]},
        // DIET={FISH=[Dish(name=prawns, vegeterian=false, calories=300, type=FISH)], MEAT=[Dish(name=chicken, vegeterian=false, calories=400, type=MEAT)], OTHER=[Dish(name=rice, vegeterian=true, calories=350, type=OTHER), Dish(name=season fruits, vegeterian=true, calories=120, type=OTHER)]},
        // NORMAL={FISH=[Dish(name=salmon, vegeterian=false, calories=450, type=FISH)], MEAT=[Dish(name=beef, vegeterian=false, calories=700, type=MEAT)], OTHER=[Dish(name=french fries, vegeterian=true, calories=530, type=OTHER), Dish(name=pizza, vegeterian=true, calories=550, type=OTHER)]}}
    }
    ~~~
###3.  자바로 DSL을 만드는 패턴과 기법
-   빌더 코드가 너무 많아서 새삼 Lombok의 소중함을 느끼게 됐다.
-   예제 도메인 모델
    1.  Stock : 주어진 시장에 주식 가격을 모델링하는 순수 자바 빈즈
    2.  Trade : 주어진 가격에서 주어진 양의 주식을 사거나 파는 거래
    3.  Order : 고객이 요청한 한 개 이상의 거래의 주문
#####3.1. 메서드 체인
-   주문 빌더 구현
    -   단점 : 빌더 전체를 구현해야 해서 작업이 많아짐
        -   Lombok에 @Builder를 이용하면 단점 해소
    ~~~
    //빌더
    public class MethodChainingOrderBuilder {    
      public final Order order = new Order();    
      private MethodChainingOrderBuilder(String customer) {
        order.setCustomer(customer);
      }    
      public static MethodChainingOrderBuilder forCustomer(String customer) {
        return new MethodChainingOrderBuilder(customer);
      }    
      public Order end() {
        return order;
      }    
      public TradeBuilder buy(int quantity) {
        return new TradeBuilder(this, Trade.Type.BUY, quantity);
      }    
      public TradeBuilder sell(int quantity) {
        return new TradeBuilder(this, Trade.Type.SELL, quantity);
      }    
      private MethodChainingOrderBuilder addTrade(Trade trade) {
        order.addTrade(trade);
        return this;
      }
    }
    //테스트
    @Test
    public void builderTest() {
        String customerName ="hj";
        MethodChainingOrderBuilder.forCustomer(customerName)
                .buy(10)
                .stock("RSUPPORT")
                .on("KOSDAQ")
                .at(5000)
                .end();
    }
    //Lombok @Builder를 적용하고 컴파일된 클래스
    public static class CustomerBuilder {
            private int id;
            private String name;    
            CustomerBuilder() {
            }    
            public Customer.CustomerBuilder id(final int id) {
                this.id = id;
                return this;
            }    
            public Customer.CustomerBuilder name(final String name) {
                this.name = name;
                return this;
            }    
            public Customer build() {
                return new Customer(this.id, this.name);
            }    
            public String toString() {
                return "Customer.CustomerBuilder(id=" + this.id + ", name=" + this.name + ")";
            }
        }
    ~~~
#####3.2. 중첩된 함수 이용
-   함수 안에 함수를 이용해 도메인 모델을 만드는 방법
-   함수의 중첩 방식이 도메인 객체 계층 구조에 그대로 반영된다는 것이 장점이다.
-   괄호가 너무 많아 지저분해 보이는게 단점
-   VO, DTO로 캡슐화 해서 넘기는게 더 좋아보일거라 생각 듬
    ~~~
    @Test
    public void nestedFunctionTest() {
        Order order = order("BigBank",
                buy(80, stock("IBM", on("NYSE")), at(125.00)),
                sell(50, stock("GOOGLE", on("NASDAQ")), at(375.00))
        );
        log.debug("nested function result : {}", order);
    }
    ~~~
#####3.3. 람다 표현식을 이용한 함수 시퀀싱
-   람다 표현식으로 정의한 함수 시퀀스를 사용하는 DSL 패턴
    -   주문(Order) 빌더, 거래(Trade) 빌더, 주식(Stock) 빌더
-   장점
    1.   메서드 체인 패턴처럼 플루언트 방식으로 거래 주문을 정의할 수 있다.
    2.   중첩 함수 형식 처럼 다양한 람다 표현식의 중첩 수준과 비슷하게 계층 구조를 유지한다.
-   단점 : 예제코드만 해도 정말 장황함
    1.  많은 설정 코드가 필요함
    2.  DSL 자체가 자바 8 람다 표현식 문법에 의한 잡음의 영향을 받음 ->
    ~~~
    //왜 이렇게까지..
    @Test
    public void functionSequencing() {
        String customer = "SEQBank";
        LambdaOrderBuilder lambdaOrderBuilder = new LambdaOrderBuilder();
        lambdaOrderBuilder.forCustomer(customer);
        Order order = LambdaOrderBuilder.order(builder -> {
            builder.forCustomer(customer);
            builder.buy(trade -> {
                trade.quantity(10);
                trade.price(10.0);
                trade.stock(stock -> {
                    stock.symbol("IBM");
                    stock.market("NYSE");
                });
            });
        });
        log.debug("order : {}", order);
        //  order : Order[customer=SEQBank, trades=[Trade[type=BUY, stock=Stock[symbol=IBM, market=NYSE], quantity=10, price=10.00]
    }
    ~~~
#####3.4. 조합하기
-   여러 DSL 패턴을 이용해 주식 거래 주문 만들기
    ~~~
    @Test
    public void mixedDSLTest() {
        String customer = "MIX";
        Order order = forCustomer(customer,
                buy(t -> t.quantity(80)
                        .stock("IBM")
                        .on("NYSE")
                        .at(125.00)),
                sell(t -> t.quantity(50)
                        .stock("GOOGLE")
                        .on("NASDAQ")
                        .at(375.00)));
        log.debug("order : {}", order);
        // order : Order[customer=MIX, trades=[
        //  Trade[type=BUY, stock=Stock[symbol=IBM, market=NYSE], quantity=80, price=125.00]
        //  Trade[type=SELL, stock=Stock[symbol=GOOGLE, market=NASDAQ], quantity=50, price=375.00]
        //]]
    }
    ~~~
#####3.5. DSL에 메서드 참조 사용하기
-   메소드 참조는 정말 코드를 간결하게 만들어 준다 멋지다
    ~~~
    @Test
    public void methodReferenceTest() {
        String customer = "MIX";
        Order order = forCustomer(customer,
                buy(t -> t.quantity(80).stock("IBM").on("NYSE").at(125.00)),
                sell(t -> t.quantity(50).stock("GOOGLE").on("NASDAQ").at(125.00)));
        double result1 =
                new TaxCalculator().withTaxRegional()
                                    .withTaxSurcharge()
                                    .calculate(order);
        Order order2 = forCustomer(customer,
                buy(t -> t.quantity(80).stock("IBM").on("NYSE").at(125.00)),
                sell(t -> t.quantity(50).stock("GOOGLE").on("NASDAQ").at(125.00)));
        double result2 =
                new TaxCalculator().with(Tax::regional)
                                    .with(Tax::surcharge)
                                    .calculateF(order2);
        //result1이랑 result2랑 값이 계속 달라서 한참보다보니 계산 함수가 다르다
        //calculate(), calculateF()
        //메소드 이름은 정말 잘 지어야 한다. 
        log.debug("result 1 : {}", result1);
        log.debug("result 2 : {}", result2);
    }
    ~~~
###4.  실생활의 자바8 DSL
DSL 패턴의 장점과 단점
-   메서드 체인 : 누가 벌써 이렇게 만들어두면 쓰기만 하는 나는 참 편하다.
    -   장점 :
        1.  메서드 이름이 키워드 인수 역할을 수행
        2.  선택형 파라미터와 잘 동작한다.
        3.  정적 메소드를 최소화하거나 없앨 수 있다.
        4.  문법적 잡음을 최소화 한다.
    -   단점 : 
        1.  구현이 장황하다.
        2.  빌드를 연결하는 접착 코드가 필요하다.
        3.  들여쓰기 규칙으로만 도메인 객체 계층을 정의한다.
-   중첩 함수
    -   장점 : 
        1.  구현의 장황함을 줄일 수 있다.
        2.  함수 중첩으로 도메인 객체 계층을 반영한다.
    -   단점 : 
        1.  정적 메서드의 사용이 빈번하다.
        2.  이름이 아닌 위치로 인수를 정의한다.
        3.  선택형 파라미터를 처리할 메소드 오버로딩이 필요하다.
-   람다를 이용한 함수 시퀀싱
    -   장점 : 
        1.  선택형 파라미터와 잘 동작한다.
        2.  정적 메소드를 최소화하거나 없앨 수 있다.
        3.  람다 중첩으로 도메인 객체 계층을 반영한다.
        4.  빌더의 접착 코드가 없다.
    -   단점 : 
        1.  구현이 장황하다.
        2.  람다 표현식으로 인한 문법적 잡음이 DSL에 존재한다.
#####4.1. jOOQ
-   SQL을 구현하는 내부적 DSL
-   자바에 내장된 형식 안전 언어
-   예시
    ~~~
    //select * from BOOK where BOOK.PUBLISHED_IN = 2006 ORDER BY BOOK.TITLE
    create.selectFrom(BOOK)
          .where(BOOK.PUBLISHED_IN.eq(2016))
          .orderBy(BOOK.TITLE)
    ~~~
#####4.2. 큐컴버
-   다른 BDD(Behavior-driven Development) 프레임워크와 마찬가지로 도메인 전용 스크립팅 언어 명령문을 실행할 수 있는 테스트케이스로 변환한다.
-   큐컴버는 개발자가 비즈니스 시나리오를 평문 영어로 구현할 수 있도록 도와주는 BDD 도구
-   예시
    ~~~
    //문장은 테스트 케이스의 변수를 캡쳐하는 정규 표현식으로 매칭된다.
    //테스트 자체를 구현하는 메소드로 문장을 전달한다.
    Feature: Buy stock
        Senario: Buy 10 IBM stocks
            Given the price of a "IBM" stock is 125$
            When I buy 10 "IBM"
            Then the order value should be 1250$
    ~~~
    ~~~
    public class BuyStocksSteps {
        //위 문장이 Given 어노테이션으로 전달되었음
        //시나리오의 전제 조건을 정의하는 부분
        @Given("^the price of a \"(.*?\" stock is (\\d+)\\$$")
        public void setUnitPrice(String stockName, int unitPrice) {
            //단가를 저장하는 세터
        }
    }
    ~~~
#####4.3. 스프링 통합(Spring Integration)
-   의존성 주입에 기반한 스프링 프로그래밍 모델을 확장한다.
-   목표 : 
    1.  복잡한 엔터프라이즈 통합 솔루션을 구현하는 단순한 모델 제공
    2.  비동기, 메시지 주도 아키텍처를 쉽게 적용할 수 있도록 도움
-   여러 엔드포인트를 한 개 이상의 메시지 흐름으로 조합해서 통합 과정이 구성된다.
    ~~~
    //예제 : https://docs.spring.io/spring-integration/docs/5.1.0.M1/reference/html/java-dsl.html
    @Configuration
    @EnableIntegration
    public class MyConfiguration {    
        @Bean
        public AtomicInteger integerSource() {
            return new AtomicInteger();
        }    
        @Bean
        public DirectChannel inputChannel() {
            return new DirectChannel
        }
        @Bean
        public IntegrationFlow myFlow() {
            return IntegrationFlows
                //integerSource를 integrationFlow의 입력으로 사용
                .from(integerSource::getAndIncrement,
                //MessageSource를 폴링하면서 MessageSource가 나르는 데이터를 가져옴
                    c -> c.poller(Pollers.fixedRate(100)))
                //inputChannel의 이름만 알고 있으면 모든 컴포넌트로 메시지를 전달할 수 있다.
                .channel(inputChannel())
                .filter((Integer p) -> p > 0)
                //MessageSource에서 가져온 정수를 문자열로 변환
                .transform(Object::toString)
                .channel(MessageChannels.queue())
                .get();
        }
    }
    ~~~
###5. 마치며
-   DSL의 주요 기능은 개발자와 도메인 전문가 사이의 간격을 좁히는 것이다.
    -   애플리케이션의 비즈니스 로직을 구현하는 코드를 만든 사람이 프로그램이 사용될 비즈니스 필드의 전문 지식을 갖추긴 어렵다.
    -   개발자가 아닌 사람도 이해할 수 있는 언어로 이런 비즈니스 로직을 구현할 수 있다고 해서 도메인 전문가가 프로그래머가 될 수 있는 것은 아니지만 적어도 로직을 읽고 검증하는 역할은 할 수 있다.
    -   DSL이랑 코드랑 별 차이 없던데 왜 이런말이 써있는지 잘 모르겠다.
-   DSL은 내부DSL, 다중DSL, 외부DSL로 구분할 수 있으며 구분 기준은 호스팅 언어를 기반으로 한 정도이다.
-   JVM에서 이용할 수 있는 스칼라, 그루비 등 다른 언어로 다중 DSL을 개발할 수 있다.
    -   장점 : 자바보다 유연하며 간결
    -   단점 : 자바와 통합할 때 빌드 과정이 복잡해지며 상호 호환성 문제 발생 가능
-   자바의 장황함과 문법적 엄격함 때문에 내부DSL 개발 언어로 적합하지 않다.
    -   자바 8의 람다 표현식과 메서드 참조 덕분에 상황이 많이 개선되었다.


---

## 플루언트 API (Fluent API)
- https://www.martinfowler.com/bliki/FluentInterface.html
- 내부 DSL 라인을 따라 작업을 수행하는 것
- 메소드 체이닝의 다른 이름인 듯
- Fluent API를 이용한 메소드체이닝 이라는 말이 있었음
- 책 읽다 보니 플루언트 방식이 메소드 체인 보다 상위 개념인듯

---

###