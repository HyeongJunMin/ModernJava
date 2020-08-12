package com.mj.modernjava.ch13;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class Ch13DefaultMethod {

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

  @Test
  public void resolutionRules() {
    FClass fClass = new FClass();
    log.info("int : {}", fClass.getInt());  // int : 10
  }
  public interface DInter extends AInter { }
  public interface EInter extends AInter { }
  public class FClass implements EInter, DInter { }
  public interface AInter {
    default int getInt() {
      return 10;
    }
  }
  public interface BInter {
    default int getInt() {
      return 20;
    }
  }
//  public class CClass implements BInter, AInter {
//    class inherits unrelated defaults for {메소드이름} from types {인터페이스1} and {인터페이스2}
//  }

}
