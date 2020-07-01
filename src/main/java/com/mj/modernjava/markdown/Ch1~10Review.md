####문제1. 
필터링+그루핑
####문제2.
DB에 있는 차들 중 status가 normal인 모든 차가 팔렸다고 할 때, 브랜드 별 매출 합계 구하기 
####문제3.
Cars의 model이름을 Set<String>으로 매핑해서 Set의 값들을 String.toUppercase()메소드를 사용해서 대문자로 바꾸고자 할 때 병렬처리, 순차처리 속도차이 측정
####문제4.
메소드파라미터화 예제... 보면 볼수록 stream으로 하는게 낫다
- 예시
    -   메소드파라미터화
        -   filter(cars, car -> "HyeonDai".equals(car.getBrand());
        -   filter(cars, car -> "Bentayga".equals(car.getModel());
    -   stream
        -   cars.stream().filter(car -> "HyeonDai".equals(car.getBrand());
####문제5.
템플릿메서드.
- 시나리오 : 
    -   모든 지점은 차량을 판매하면 status를 null로 바꿔주는 기능을 사용하고 있는 상황을 가정
    -   특정 지점만 status뿐 만 아니라 차량색도 바꾸고 싶다고 요청이 들어온 경우
    -   가까운 미래에 판매된 차량의 status를 null이 아니라 soldout으로 변경하도록 로직을 수정할 예정이므로 상속필요
- 결과
    -   템플릿 메서드 패턴과 람다를 사용해보려고 했는데
    -   오버로딩이 더 좋았다