package com.mj.modernjava.ch9;


//1번 방법 : 각 지점은 OnlineBanking클래스를 상속받아 각기 다른 방법으로 Customer를 행복하게 한다
public abstract class OnlineBanking {
  //매개변수로 받은 id에 해당하는 Customer를 행복하게 해주는 메소드
  public void processCustomer(int id) {
    Customer c = Customer.builder().id(id).build();
    makeCustomerHappy(c);
  }
  abstract void makeCustomerHappy(Customer c);
}
