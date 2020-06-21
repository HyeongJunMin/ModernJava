package com.mj.modernjava.ch10;

import com.mj.modernjava.ch10.model.Order;
import com.mj.modernjava.ch10.model.Stock;
import com.mj.modernjava.ch10.model.Tax;
import com.mj.modernjava.ch10.model.Trade;
import com.mj.modernjava.common.Dish;
import com.mj.modernjava.common.Menu;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static com.mj.modernjava.ch10.MixedBuilder.forCustomer;
import static com.mj.modernjava.ch10.MixedBuilder.*;
import static com.mj.modernjava.ch10.NestedFunctionOrderBuilder.*;
import static java.util.stream.Collectors.groupingBy;

@Slf4j
public class Ch10DSL {

    @Test
    public void innerDSLTest() {
        List<String> numbers = Arrays.asList("one", "two", "three");
        //익명 내부 클래스
        numbers.forEach(new Consumer<String>() {
            @Override
            public void accept(String s) {
                log.debug(s);
            }
        });
        //메소드 참조
        numbers.forEach(log::debug);
    }

    @Test
    public void multipleGroupingTest() {
        List<Dish> menuList = Menu.getList();
        Map<Dish.CaloricalLevel, Map<Dish.Type, List<Dish>>> collect
                = menuList.stream().collect(groupingBy(Dish::getCaloricalLevel, groupingBy(Dish::getType)));
        log.debug("result : {}", collect.toString());
        //result : {
        // FAT={MEAT=[Dish(name=pork, vegeterian=false, calories=800, type=MEAT)]},
        // DIET={FISH=[Dish(name=prawns, vegeterian=false, calories=300, type=FISH)], MEAT=[Dish(name=chicken, vegeterian=false, calories=400, type=MEAT)], OTHER=[Dish(name=rice, vegeterian=true, calories=350, type=OTHER), Dish(name=season fruits, vegeterian=true, calories=120, type=OTHER)]},
        // NORMAL={FISH=[Dish(name=salmon, vegeterian=false, calories=450, type=FISH)], MEAT=[Dish(name=beef, vegeterian=false, calories=700, type=MEAT)], OTHER=[Dish(name=french fries, vegeterian=true, calories=530, type=OTHER), Dish(name=pizza, vegeterian=true, calories=550, type=OTHER)]}}
    }

    @Test
    public void builderTest() {
        String customerName ="hj";
        MethodChainingOrderBuilder.forCustomer(customerName)
                .buy(10)
                .stock("RSUPPORT")
                .on("KOSDAQ")
                .at(5000)
                .end();
    }

    @Test
    public void nestedFunctionTest() {
        String customer = "BigBank";
        Order order = order(customer,
                buy(80, stock("IBM", on("NYSE")), at(125.00)),
                sell(50, stock("GOOGLE", on("NASDAQ")), at(375.00))
        );
        log.debug("nested function result : {}", order);
        OrderVO orderVO = new OrderVO();
        orderVO.setCustomer(customer);
        orderVO.getTradeList().add(new Trade(Trade.Type.BUY, new Stock("IBM", "NYSE"), 80, 125.00));
        Order orderByVO = Order.newInstance(orderVO);
        log.debug("order by VO : {}", orderVO);
    }

    @Test
    public void functionSequencingTest() {
        String customer = "SEQBank";
        LambdaOrderBuilder lambdaOrderBuilder = new LambdaOrderBuilder();
        lambdaOrderBuilder.forCustomer(customer);
        Order order = LambdaOrderBuilder.order(builder -> {
            builder.forCustomer(customer);
            builder.buy(trade -> {
                trade.quantity(10);
                trade.price(10.0);
                trade.stock(stock -> {
                    stock.symbol("IBM");
                    stock.market("NYSE");
                });
            });
        });
        log.debug("order : {}", order);
        //  order : Order[customer=SEQBank, trades=[Trade[type=BUY, stock=Stock[symbol=IBM, market=NYSE], quantity=10, price=10.00]
    }

    @Test
    public void mixedDSLTest() {
        String customer = "MIX";
        Order order = forCustomer(customer,
                buy(t -> t.quantity(80)
                        .stock("IBM")
                        .on("NYSE")
                        .at(125.00)),
                sell(t -> t.quantity(50)
                        .stock("GOOGLE")
                        .on("NASDAQ")
                        .at(375.00)));
        log.debug("order : {}", order);
        // order : Order[customer=MIX, trades=[
        //  Trade[type=BUY, stock=Stock[symbol=IBM, market=NYSE], quantity=80, price=125.00]
        //  Trade[type=SELL, stock=Stock[symbol=GOOGLE, market=NASDAQ], quantity=50, price=375.00]
        //]]
    }

    @Test
    public void methodReferenceTest() {
        String customer = "MIX";
        Order order = forCustomer(customer,
                buy(t -> t.quantity(80).stock("IBM").on("NYSE").at(125.00)),
                sell(t -> t.quantity(50).stock("GOOGLE").on("NASDAQ").at(125.00)));
        double result1 =
                new TaxCalculator().withTaxRegional()
                                    .withTaxSurcharge()
                                    .calculate(order);
        Order order2 = forCustomer(customer,
                buy(t -> t.quantity(80).stock("IBM").on("NYSE").at(125.00)),
                sell(t -> t.quantity(50).stock("GOOGLE").on("NASDAQ").at(125.00)));
        double result2 =
                new TaxCalculator().with(Tax::regional)
                                    .with(Tax::surcharge)
                                    .calculateF(order2);
        log.debug("result 1 : {}", result1);
        log.debug("result 2 : {}", result2);
    }
}
