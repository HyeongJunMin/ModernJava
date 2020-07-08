####문제1. 
필터링+그루핑
최대속도, 최소가격, 최대가격, 추천 모델 수를 고객에게 받아서 그에 맞는 모델과 재고를 추천할 수 있는 기능 개발
     - 예시) 최대속도 250km/h 이상의 차량 중에서 가격 2억5천만원~3억까지의 모델 3종류의 재고를 추천
####문제2.
DB에 있는 차들 중 status가 normal인 모든 차가 팔렸다고 할 때, 브랜드 별 매출 합계 구하는 기능 개발 
####문제3.
Cars의 model이름을 Set으로 매핑해서 Set의 값들을 String.toUppercase()메소드를 사용해서 대문자로 바꾸고자 할 때 병렬처리, 순차처리 속도차이 측정해보기
####문제4.
시나리오에 해당하는 기능 개발(메소드 파라미터화)
    - 모든 지점은 차량을 판매하면 status를 null로 바꿔주는 기능을 사용하고 있는 상황을 가정 
    - 특정 지점만 status뿐 만 아니라 차량색도 바꾸고 싶다고 요청이 들어온 경우 
    - 가까운 미래에 판매된 차량의 status를 null이 아니라 soldout으로 변경하도록 로직을 수정할 예정이므로 상속필요
####문제5.
템플릿메서드.
- 시나리오 : 
    -   모든 지점은 차량을 판매하면 status를 null로 바꿔주는 기능을 사용하고 있는 상황을 가정
    -   특정 지점만 status뿐 만 아니라 차량색도 바꾸고 싶다고 요청이 들어온 경우
    -   가까운 미래에 판매된 차량의 status를 null이 아니라 soldout으로 변경하도록 로직을 수정할 예정이므로 상속필요
- 결과
    -   템플릿 메서드 패턴과 람다를 사용해보려고 했는데
    -   오버로딩이 더 좋았다
    
#### 추가
메소드파라미터화 예제... 보면 볼수록 stream으로 하는게 낫다
- 예시
    -   메소드파라미터화
        -   filter(cars, car -> "HyeonDai".equals(car.getBrand());
        -   filter(cars, car -> "Bentayga".equals(car.getModel());
    -   stream
        -   cars.stream().filter(car -> "HyeonDai".equals(car.getBrand());
        
        
  
insert into cars values ('49사3940'  , 'black', 'rollsroyce', 'Wraith', 250, 17020, 1507,  5285, 1947, 82, 22, 401000000, 2016, 'coupe', '20231010', 'repairing');