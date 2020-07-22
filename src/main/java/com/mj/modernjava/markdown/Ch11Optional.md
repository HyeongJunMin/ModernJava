# 목차

> 요약 및 결론  
> 책 내용  

---

#### 요약 및 결론
- Optional은 만능이 아니다. null 체크는 Optional이 있든 없든 더럽다.
- 'null일 수도 있는 값이다'라는 의미를 전달하는 역할이 중요한 듯 하다.
- flatMap 연결, ifPresent은 참 좋아 보인다.
---

#### 책 내용

1. Optional 클래스
    1. 선택형값을 캡슐화하는 클래스
        - 값이 있는 경우 : Optional 클래스는 값을 감싼다
        - 값이 없는 경우 : Optional을 반환
    2. 모든 null 참조를 Optional로대치하는 것은 바람직하지 않다.
        - ***Optional의 역할은 더 이해하기 쉬운  API를 설계하도록 돕는 것이다.***
2. Optional 적용 패턴
    - Optional 객체를 만드는 여러 방법 
    ~~~    
    @Test
    void makeOptional() {
      //  빈 Optional 객체
      Optional<Cars> empty = Optional.empty();
   
      //  null이 아닌값으로 Optional 만들기
      //  of()는 null이 아닌 값을 포함하는 Optional을 반환해주는 메소드이기 때문에 car가 null이면 즉시 NPE 발생
      //  Objects 클래스에서 NPE throw
      Cars car = null;
      Optional<Cars> notNullCar = Optional.of(car); //  NPE!
   
      //  null값을 저장할 수 있는 Optional을 만드는 ofNullable()
      Optional<Cars> nullableCar = Optional.ofNullable(car);
    }
    ~~~
    - Optional 객체의 값을 다루는 여러 방법
    ~~~    
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
    ~~~
    - Optional.flatMap() 활용방법 : 중첩Optional 제어
    ~~~    
    @Test
    void flatMapTest() {
      Insurance insurance = Insurance.builder().name("삼성화재").build();
      Car car = Car.builder().insurance(insurance).name("기블리").build();
      Person person = Person.builder().car(car).name("민형준").build();
    
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
    ~~~
    - 디폴트 액션과 Optional 언랩
    ~~~
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
    ~~~
    - 두 Optional 합치기
    ~~~
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
    ~~~
    - Optional.stream()은 자바 9 부터 제공
3. Optional 실용 예제
    - 잠재적으로 null이 될 수 있는 대상 Optional로 감싸기
    ~~~
    @Test
    void wrapNullableValue() {
      // Map에서 key없는경우 위험부담 줄이기
      Map<String, Person> map = new HashMap();
      Optional<Person> optionalPerson = Optional.ofNullable(map.get("a"));
    }
    ~~~
    - 예외와 Optional 클래스
      -  Optional과 예외 함께 활용
    ~~~
    public static Optional<Integer> stringToInt(String s) {
      try {
        return Optional.of(Integer.parseInt(s));
      } catch (NumberFormatException e) {
        return Optional.empty();
      }
    }
    ~~~
    -  기본형 Optional을 사용하지 말아야 하는 이유
      -  Stream이 많은 요소에서는 기본형 특화 스트림이 성능개선에 좋지만, Optional의 최대 요소 수는 한 개 이므로 성능 개선 불가
      -  기본형 특화 스트림은 map(), flatMap(), filter()등을 지원하지 않음
      ~~~
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
      ~~~
#### 


#### Optional과 Serialize
- Optional의 용도는 선택형 반환값을 지원하는 것이다.
- Serializable을 구현하지 않는다.
  -  실제로 String, Integer(Number) 등은 Serializable을 구현하고 있다.
- 필드 형식으로 사용할 것을 가정하지 않았기 때문에 모델에 Optional을 필드로 둔다면 직렬화 과정에 문제가 생길 수 있다.
  -  무슨 문제? 인지는 모르겠음
- 조금 찾아보니 필드로 선언 하라고 만든게 아닌데 굳이 왜 쓰느냐 라는 식임