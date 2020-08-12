# 목차

> 요약 및 결론  
> 책 내용  

---

#### 요약 및 결론
- interface의 의미를 퇴색시키는 요소 아닌가?
- 버전업 하는데 그정도는 할 수 있지 않나.. 아니면 업데이트 툴을 만들어 줘도 될것 같은데 그게 귀찮았나?
- 동작 다중 상속은 꽤 좋은 기능인듯 하다
- 다이아몬드 문제 컴파일 오류 안나는데?
---

#### 책 내용
0. default method?
1. API는 바꾸기 어렵다.
    - 인터페이스에 새로운 메서드를 추가해도 바이너리 호환성은 유지된다.
        - 새로 추가된 메서드를 호출하지만 않으면 새로운 메서드 구현이 없어도 기존 클래스 파일 구현이 잘 동작한다는 의미
        - 메서드 추가하고 컴파일하면 당연히 오류나고 여기서 말하는건 .java말고 .class인듯
    - 하지만 소스호환성은 깨진다
        - .java를 바꿨는데도 컴파일잘된다? 소스호환성이 유지된다고 하면됨
    
2. 디폴트메서드란?
    - 호환성을 유지하면서 API를 바꿀 수 있게끔 해주는 새로운 기능
3. 디폴트 메서드 활용 패턴
    - 선택형 메서드
        - 예시 : Iterator.remove()
        - 인터페이스의 메서드 중 잘 사용되지 않는 메서드가 있으면 구현클래스에서 빈 구현을 제공할 필요가 없도록 default메서드로 정의
        - 잘 안쓰는건 누가 정하는거고 진짜 잘 안쓰이면 알아서 구현하라고 두면 되지 않나
    - 동작 다중 상속
        - 인터페이스는 다중으로 구현할 수 있기 때문에 여러 개의 인터페이스에 구현되어 있는 default메서드를 사용할 수 있다
        - 동작 다중 상속 없어도 되도록 구조를 잡는게 가장 좋을거라고 생각이 들지만
        - 만약 꼭 이런 기능이 필요한 상황이 온다면 아주 좋은 활용방법으로 보인다        
        ```
        @Test
        public void multipleImplement() {
          // Moveable, Resizable, Rotatable 을 모두 구현한 Monster class
          Monster monster = new Monster();
          monster.moveHorizontally(5);  //Moveable의 default method
          monster.setRelativeSize(5, 5);  //Resizable의 default method
          monster.rotateBy(120);  //Rotatable의 default method
          // Moveable, Rotatable 두 개의 interface만 구현한 Sun class
          Sun sun = new Sun();
          sun.moveHorizontally(10);  //Moveable의 default method
          // sun.setRelativeSize(10, 10);  //Resizable의 default method
          sun.rotateBy(240);  //Rotatable의 default method
        }
        ```
4. 해석규칙
    - 구현한 인터페이스들이 갖는 디폴트 메소드 중에 메소드 시그니처 중복이 있었다면?
    - 자바 컴파일러가 해결
    - 규칙
        1. 클래스가 항상 이긴다.클래스나 슈퍼클래스에서 정의한 메서드가 디폴트 메서드보다 우선권을 갖는다.
        2. 서브인터페이스가 이긴다. 즉 B extends A 라면 B가 A를 이긴다.
        3. 위의 두 규칙으로 해결되지 않는다면 명시적으로 디폴트 메서드를 오버라이드하고 호출해야 한다.
            - 오버라이드 하지 않으면? -> intellij 빨간줄
            - 빨간줄 내용 : class inherits unrelated defaults for {메소드이름} from types {인터페이스1} and {인터페이스2}
    - 다이아몬드문제
        - 컴파일에러 난다면서..? 안나는데?
        ```
        @Test
        public void resolutionRules() {
          FClass fClass = new FClass();
          log.info("int : {}", fClass.getInt());
        }
        public interface DInter extends AInter { }
        public interface EInter extends AInter { }
        public class FClass implements EInter, DInter { }
        ```
