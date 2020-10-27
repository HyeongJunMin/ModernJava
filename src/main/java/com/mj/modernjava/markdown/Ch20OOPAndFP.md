# 목차

> 요약 및 결론  
> 책 내용  

---

## 요약 및 결론
- 스칼라와 자바에 적용된 함수형의 기능을 살펴 보면서 자바의 한계를 확인
- 간단한 스칼라 예제코드 실행 : https://scastie.scala-lang.org/

## 책 내용
### 1. 스칼라 소개
> 명령형, 함수형으로 구현된 Hello World예제 확인,
> 
> 스칼라의 자료구조 확인(List, Set, Map, Stream, Tuple, Option...)
1. Hello Beer
    - 스칼라에서는 모든 것이 객체다. = 기본자료형(primitive)이 없다성
    - 스칼라는 자동으로 변수형을 추론하는 기능이 있다.
    - 모든 변수의 형식은 컴파일 할 때 결정된다.
    ```
    // object로 클래스를 정의하고 동시에 싱글턴 객체를 만들었다.
    object Beer {
      // object 내부에 선언된 메서드는 정적 메서드로 간주되므로 static을 명시할 필요 없다.
      def main(args: Array[String]) {
        var n : Int = 2
        while (n <= 6) {
          // 스칼라의 문자열 보간법 : 문자열에 접두어s를 붙이고, ${}안에 변수 위치
          println(s"hello ${n}")
          n += 1
        }
      }
    }
    object Beer {
      def main(args: Array[String]) {
        2 to 6 foreach { n => println(s"hello ${n}") }
        // 2.to(6).foreach(n => println(s"hello ${n}"))  //위와 동일
      }
    }
    ```
2. 기본 자료구조 : 리스트, 집합, 맵, 튜플, 스트림, 옵션
    - 컬렉션 만들기
    ```
    object Beer {
      def main(args: Array[String]) {
        // Map 선언 result : Some(23)
        var authorsToAge = Map("R" -> 23, "M" -> 40,  "A" -> 54)
        println(s"${authorsToAge.get("R")}")
        // List 선언 result : R M A 
        val authors = List("R", "M", "A")
        authors.foreach(name => print(s"${name} "))
        // Set 선언 result : 5 1 6 2 3 
        val numbers = Set(1, 1, 2, 3, 5, 6)
        numbers.foreach(number => print(s"${number} "))
      }
    }
    ```
    - 불변과 가변
        - 위 예제에서 만든 자료구조들은 불변객체
        - 갱신이 필요하면 새로운 컬렉션을 만들어야 함
    ```
    object Beer {
      def main(args: Array[String]) {
        val authors = Set("R", "M", "A")
        val newAuthors = authors + "B"
        println(newAuthors) // Set(R, M, A, B)
      }
    }
    ```
    - 컬렉션 사용하기
    ```
    object Beer {
      def main(args: Array[String]) {
        val numbers = List("three", "four", "seven")
        val result = numbers.filter(n => n.length() > 4).map(n => n.toUpperCase())
        println(result)
        // List(THREE, SEVEN)
        val resultByUnderscore = numbers filter (_.length > 4) map (_.toUpperCase)
        println(resultByUnderscore)
        // List(THREE, SEVEN)
      }
    }
    ```
    - 튜플
        - 자바는 튜플을 지원하지 않는다. 필요하면 VO를 만들어써야지
        - 자바 14부터 튜플식으로 쓸 수 있는 record 지원
    ```
    object Beer {
      def main(args: Array[String]) {
        val book = (2018, "Modern Java", "Manning")
        val numbers = (2, 3, 4, 5)
        println(book) // (2018,Modern Java,Manning)
        println(numbers) // (2,3,4,5)
      }
    }
    ```
    - 스트림
        - 스칼라의 스트림은 자바의 스트림보다 다양한 기능을 제공한다.
        - 이전 요소가 접근할 수 있도록 기존 계산 값을 기억한다.
        - 인덱스를 제공하기 때문에 리스트처럼 인덱스로 스트림 요소에 접근할 수 있다.
        - 자바의 스트림에 비해 메모리 효율성이 떨어진다.
    - 옵션
        - Optional 친구
    ```
    def getCarInsuranceName(person: Option[Person], minAge: Int) =
      person.filter(_.getAge >= minAge)
            .flatMap(_.getCar)
            .flatMap(_.getInsurance)
            .map(_.getName)
            .getOrElse("Unknown")
    ```
### 2. 함수
> 스칼라의 함수는 어떤 작업을 수행하는 명령어 그룹이다.

```
살펴볼 내용들
1. 함수 형식 : 함수형 인터페이스에 선언된 추상 메서드의 시그니처를 표현하는 개념
2. 익명 함수 : 자바의 람다 표현식과 달리 비지역 변수 기록에 제한을 받지 않는다.
3. 커링 지원 : 여러 인수를 받는 함수를 일부 인수를 받는 여러 함수로 분리
```
1. 스칼라의 일급 함수
    - Predicate함수를 저장해뒀다가 filter함수에 인수로 전달할 수 있다.
    ```
    object Beer {
      def main(args: Array[String]) {
        def isJavaMentioned(tweet: String) : Boolean = tweet.contains("Java")
        val tweets = List("Java", "Scala")
        tweets.filter(isJavaMentioned).foreach(println)
      }
    }
    ```
2. 익명 함수와 클로저
    - scala.Function1형식의 익명 클래스를 축약한 다음 Function1의 apply호출
    ```
    object Beer {
      def main(args: Array[String]) {
        val isLong : String => Boolean = (tweet : String) => tweet.length > 60
        var result = isLong.apply("short tweet")
        println(result)
      }
    }
    ```
    - 클로저
        - 함수의 비지역 변수를 자유롭게 참조할 수 있는 함수의 인스턴스.
        - 자바의 람다에서는 람다가 정의된 지역 변수를 final로 취급해서 고칠 수 없는 제약이 있다.
    ```
    object Beer {
      def main(args: Array[String]) {
        var count = 0
        // 람다 표현식인데 지역변수를 수정했다!
        val inc = () => count += 1
        inc()
        println(count)  // 1
        inc()
        println(count)  // 2
      }
    }
    ```
3. 커링
    - 스칼라에서는 커리된 함수를 직접 제공할 필요가 없다.
    - 함수가 여러 커리된 인수 리스트를 포함하고 있음을 가리키는 함수 정의 문법을 제공하기 때문이다.
    ```
    object Beer {
      def main(args: Array[String]) {
        // 인수 리스트 둘을 받는 함수
        def multiply(x : Int, y : Int) = x * y
        println(multiply(2, 10))  // 20
        // 파라미터 둘로 구성된 인수 리스트 하나를 받는 함수
        def multiplyCurry(x : Int)(y : Int) = x * y
        println(multiplyCurry(2)(10)) // 20
      }
    }
    ```
### 3. 클래스와 트레이트
> 자바의 클래스와 인터페이스를 스칼라와 비교
1. 간결성을 제공하는 스칼라의 클래스
    - 스칼라는 완전한 객체지향 언어이므로 클래스를 만들고 객체로 인스턴스화 할 수 있다.
    - 생성자, 게터, 세터가 암시적으로 생성된다.
    - private var name해놓고 hello.name하면 안됨. hello.getName()도 없어서 새로만들어줘야됨
    - 생성자는 ㅇㅋ
    ```
    object Beer {
      def main(args: Array[String]) {
        val hello = new Hello("dd");
        hello.sayThankYou // Thanks
        hello.name = "aa" // 이게 왜 세터?
        println(hello.name) // 이게 왜 게터?
      }
      class Hello(var name: String) {
        def sayThankYou() {
          println("Thanks")
        }
      }
    }
    ```
2. 스칼라 트레이트와 자바 인터페이스
    - 트레이트 : 추상화 기능으로 자바의 인터페이스와 유사
    - 추상 메서드와 기본 구현을 가진 메서드 모두 정의할 수 있음
    - 다중 상속 지원
    - 인스턴스화 과정에서도 조합할 수 있음. 신기하다
    ```
    object Beer {
      def main(args: Array[String]) {
        val isEmpty = new Empty().isEmpty();
        println(isEmpty)  //  true
        val sizedHello = new Hello("a") with Sized
        println(sizedHello.isEmpty()) //  true
      }
      class Hello(var name: String) {
        def sayThankYou() {
          println("Thanks")
        }
      }
      trait Sized {
        var size : Int = 0  // 필드
        def isEmpty() = size == 0  // 기본 구현을 제공하는 메서드
      }
      class Empty extends Sized { }
    }
    ```
### 4. 마치며
- 자바와 스칼라는 객체지향과 함수형 프로그래밍 모두를 하나의 프로그래밍 언어로 수용한다.
- 두 언어 모두 JVM에서 실행되며 넓은 의미에서 상호운용성을 갖는다.
- 스칼라는 자바처럼 다양한 추상 컬렉션을 제공한다. 튜플도(자바14에서는 자바도 record라고 튜플같은거 있음)
- 스칼라는 자바에 비해 풍부한 함수 관련 기능을 제공한다.
    - 함수 형식, 클로저, 내장 커링 형식
    - 풍부한 것 까지는 아니고 몇개 더 있나 본데 클로저 말고는 딱히..
- 스칼라는 트레이트를 지원한다. 필드와 디폴트 메서드를 포함할 수 있는 인터페이스이다.
    - 좋은지는 모르겠다. 