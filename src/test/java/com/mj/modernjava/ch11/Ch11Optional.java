package com.mj.modernjava.ch11;

import com.mj.modernjava.common.Car;
import com.mj.modernjava.common.Insurance;
import com.mj.modernjava.common.Person;
import com.mj.modernjava.review.domain.Cars;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class Ch11Optional {

  private Insurance insurance = Insurance.builder().name("삼성화재").build();
  private Car car = Car.builder().insurance(insurance).name("기블리").build();
  private Person person = Person.builder().car(car).name("민형준").build();

  @Test
  void makeOptional() {
    //  빈 Optional 객체
    Optional<Cars> empty = Optional.empty();
    //  null이 아닌값으로 Optional 만들기
    //  of()는 null이 아닌 값을 포함하는 Optional을 반환해주는 메소드이기 때문에 car가 null이면 즉시 NPE 발생
    //  Objects 클래스에서 NPE throw
    Cars car = null;
    Optional<Cars> notNullCar = Optional.of(car);
    //  null값을 저장할 수 있는 Optional을 만드는 ofNullable()
    Optional<Cars> nullableCar = Optional.ofNullable(car);
  }

  @Test
  void treatValuesByOptionalMapTest() {
    // Optional.map()
    Cars car = null;
    Optional<String> optionalModel = Optional.ofNullable(car).map(Cars::getModel);
    log.info("optional model : {}", optionalModel); // 결과 : optional model : Optional.empty
    String nullString = null;
    Optional.ofNullable(nullString).ifPresent(optional ->
        log.info("is present : {}", optional)); // 결과 : 출력 없음
    String notNullString = "str";
    Optional.ofNullable(notNullString).ifPresent(optional ->
        log.info("is present : {}", optional)); // 결과 : is present : str
  }

  @Test
  void flatMapTest() {
    //  Optional 없이 insurance의 name 확인
    if (person.getCar() != null) {
      if (car.getInsurance() != null) {
        log.info("insurance : {}", insurance.getName());
      } else {
        log.info("Unknown");
      }
    }

    //  컴파일 불가
    //  Optional<String> insuranceName = Optional.of(person)
    //      .map(Person::getCar)  // Optional<Optional<Car>>를 리턴
    //      .map(Car::getInsurance)
    //      .map(Insurance::getName);

    String unknown = Optional.ofNullable(person)
        .flatMap(Person::getCar)
        .flatMap(Car::getInsurance)
        .map(Insurance::getName)
        .orElse("Unknown");
    log.info("unknown : {}", unknown);
  }

  @Test
  void defaultActionAndUnwrap() {
    // 값이 있느냐 없느냐의 기준은 null이냐 아니냐
    Person getTestPerson = new Person();
    Optional.ofNullable(getTestPerson).get(); // 통과
    getTestPerson = null;
    //  Optional.ofNullable(getTestPerson).get(); // NoSuchElementException
    //
    Optional<Person> optionalPerson = Optional.ofNullable(person);
    // get() : 래핑된 값이 있으면 해당 값을 반환하고, 값이 없으면 NoSuchElementException 발생
    optionalPerson.get();
    // orElse() : 값 없을 때 리턴할 대체값을 매개변수로 전달
    optionalPerson.orElse(new Person());
    // orElseGet() : orElse의 매개변수는 Optional의 값이 있든 없든 항상 호출되지만 orElseGet은 Optional의 값이 없으면 호출안됨
    // Person.newInstance(String num)은 num을 출력하고 빈 Person객체를 생성해서 리턴해주는 스태틱 메소드
    optionalPerson.orElse(Person.newInstance("1'"));  // 결과 : num : 1
    optionalPerson.orElseGet(() -> Person.newInstance("2"));  // 결과 : 출력 없음
    // orElseThrow() : Optional이 비었을 때 매개변수로 들어간 예외 발생
    optionalPerson.orElseThrow(() -> new NumberFormatException());
    // ifPresent() : 값이 존재할 때 인수로 넘겨준 동작 실행
    optionalPerson.ifPresent(person -> log.info("is present"));
    // [Java 9]ifPresentOrElse() : Optional이 비었을 때 실행할 수 있는Runnable을 받음
  }

  @Test
  void mergeOptionalObjects() {
    Optional<Person> optionalPerson = Optional.ofNullable(this.person);
    Optional<Car> optionalCar = Optional.ofNullable(this.car);
    // 합체없음
    if (optionalPerson.isPresent() && optionalCar.isPresent()) {
      Optional.of(findCheapestInsurance(optionalPerson.get(), optionalCar.get()));
    }
    // 합체
    // p가 빈 Optional이면 빈 Optional 반환, c가 빈 Optional이면 빈 Optional 반환
    // 둘 중 하나라도 빈 Optional이면 빈 Optional을 반환하고 아니면 findCheapestInsurance수행
    optionalPerson.flatMap(p ->  optionalCar.map(c -> findCheapestInsurance(p, c)));
  }

  private Optional<Insurance> findCheapestInsurance(Person person, Car car) {
    return car.getInsurance();
  }

  @Test
  void wrapNullableValueTest() {
    // Map에서 key없는경우 위험부담 줄이기
    Map<String, Person> map = new HashMap();
    Optional<Person> optionalPerson = Optional.ofNullable(map.get("a"));
  }

  public static Optional<Integer> stringToInt(String s) {
    try {
      return Optional.of(Integer.parseInt(s));
    } catch (NumberFormatException e) {
      return Optional.empty();
    }
  }

  @Test
  void primitiveOptionalTest() {
    OptionalInt.of(5).equals(1);
    OptionalInt.of(5).getAsInt();
    OptionalInt.of(5).ifPresent(i -> log.info("ok"));
    OptionalInt.of(5).isPresent();
    OptionalInt.of(5).orElse(1);
    OptionalInt.of(5).orElseGet(() -> new Integer(1));
    OptionalInt.of(5).orElseThrow(() -> new NumberFormatException());
  }

}
