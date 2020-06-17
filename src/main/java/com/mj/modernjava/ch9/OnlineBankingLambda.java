package com.mj.modernjava.ch9;

import java.util.function.Consumer;

//2번 방법 : 각 지점은 OnlineBanking클래스를 상속받지 않고 람다표현식을 통해 직접 다양한 동작을 추가할 수 있다.
public class OnlineBankingLambda {
  //
  public void processCustomer(int id, String name, Consumer<Customer> makeCustomerHappy) {
    Customer c = Customer.builder().id(id).name(name).build();
    makeCustomerHappy.accept(c);
  }
}
