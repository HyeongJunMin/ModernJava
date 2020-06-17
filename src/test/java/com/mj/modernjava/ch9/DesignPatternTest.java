package com.mj.modernjava.ch9;

import java.util.function.Function;
import java.util.function.UnaryOperator;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class DesignPatternTest<p2> {

  @Test
  public void strategyPatternTest() {
    Validator numericValidator = new Validator(new IsNumeric());
    Validator numericLambda = new Validator(s -> s.matches("[a-z]+"));
    boolean isNumeric = numericValidator.validate("aaaa");
    log.debug("is numeric : {}", isNumeric);  //is numeric : false

    Validator caseValidator = new Validator(new IsAllLowerCase());
    Validator caseLambda = new Validator(s -> s.matches("\\d+"));
    boolean isLower = caseValidator.validate("aaaa");
    log.debug("is lower case : {}", isLower); //is lower case : true
  }

  @Test
  public void templateMethodTest() {
    new OnlineBankingLambda().processCustomer(1337, "hj", (Customer c) ->
        log.debug("Hello {}", c.getName()));  //Hello hj
  }

  @Test
  public void observerTest() {
    //구현한 Observer패턴 테스트코드
    Feed f = new Feed();
    String tweet = "The queen said her favourite book is Modern Java in Action!";
    f.registerObserver(new NYTimes());
    f.registerObserver(new Guardian());
    f.registerObserver(new LeMonde());
    f.notifyObservers(tweet);
    //결과 : Yet more news from London... The queen said her favourite book is Modern Java in Action!
    //
    //람다표현식 사용
    //명시적으로 인스턴스화 하지 않고 람다 표현식을 직접 전달해서 실행할 동작을 지정할 수 있음
    f.registerObserver((String t) -> {
      if (t != null && t.contains("book")) {
        System.out.println("Study hard please... " + t);
      }
    });
    f.notifyObservers(tweet);
    //결과 : Study hard please... The queen said her favourite book is Modern Java in Action!
  }


@Test
public void chainOfResponsibilityPatternTest() {
  ProcessingObject<String> p1 = new HeaderTextProcessing();
  ProcessingObject<String> p2 = new SpellCheckerProcessing();
  p1.setSuccessor(p2);
  log.info("p1 result : {}", p1.handle("labdas"));  //result : header text
  log.info("p2 result : {}", p2.handle("labdas"));  //p2 result : lambdas
  //
  //람다 표현식 사용
  UnaryOperator<String> headerProcessing = (String text) -> "Header text " + text;
  UnaryOperator<String> spellCheckerProcessing = (String text) -> text.replaceAll("labda", "lambda");
  Function<String, String> pipeLine = headerProcessing.andThen(spellCheckerProcessing);
  String result = pipeLine.apply("labda");
  log.info("result : {}", result);  //result : Header text lambda
}

}
